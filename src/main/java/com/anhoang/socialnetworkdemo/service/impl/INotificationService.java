package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.entity.*;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.model.notify.PostCommentNotifyDto;
import com.anhoang.socialnetworkdemo.model.notify.PostNotifyDto;
import com.anhoang.socialnetworkdemo.model.notify.UserNotifyDto;
import com.anhoang.socialnetworkdemo.payload.PageData;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.payload.socket_payload.NotificationData;
import com.anhoang.socialnetworkdemo.payload.socket_payload.SocketBody;
import com.anhoang.socialnetworkdemo.repository.NotificationRepository;
import com.anhoang.socialnetworkdemo.repository.PostCommentRepository;
import com.anhoang.socialnetworkdemo.repository.PostRepository;
import com.anhoang.socialnetworkdemo.repository.UsersRepository;
import com.anhoang.socialnetworkdemo.service.NotificationService;
import com.anhoang.socialnetworkdemo.utils.AuthenticationUtils;
import com.anhoang.socialnetworkdemo.utils.TimeMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class INotificationService implements NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final UsersRepository usersRepository;
    private final NotificationRepository notifyRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository commentRepository;
    private final AuthenticationUtils authUtils;

    public NotificationData entityToNotifyData(Notifications notify, Object targetData){
        return NotificationData.builder()
                .notifyId(notify.getId())
                .type(notify.getType())
                .content(notify.getContent())
                .isRead(notify.getIsRead())
                .createdAt(TimeMapperUtils.formatFacebookTime(notify.getCreatedAt()))
                .targetData(targetData)
                .otherUrl(null)
                .build();
    }

    @Async
    @Override
    @Transactional
    public void sendFriendshipNotifyToUser(Notifications.Type type, String senderCode,
                                           String userCode, LocalDateTime createdAt) {
        try{
            Users user = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(() -> new RequestNotFoundException("User not found!"));
            Users senderUser = usersRepository.findUsersByUserCode(senderCode)
                    .orElseThrow(() -> new RequestNotFoundException("Sender user not found!"));

            Notifications notify = new Notifications();
            if(type==Notifications.Type.FRIEND_REQUEST){
                notify.setType(Notifications.Type.FRIEND_REQUEST);
                notify.setContent(" đã gửi cho bạn lời mời kết bạn.");
            } else if(type==Notifications.Type.FRIEND_ACCEPTED){
                notify.setType(Notifications.Type.FRIEND_ACCEPTED);
                notify.setContent(" đã chấp nhận lời mời kết bạn của bạn.");
            } else if(type==Notifications.Type.FOLLOW_REQUEST){
                notify.setType(Notifications.Type.FOLLOW_REQUEST);
                notify.setContent(" đã bắt đầu theo dõi bạn.");
            } else{
                throw new RequestNotFoundException("Error");
            }
            notify.setIsRead(false);
            notify.setUser(user);
            notify.setSenderUser(senderUser);
            notify.setCreatedAt(createdAt);
            notify.setUpdatedAt(createdAt);
            notify = notifyRepository.save(notify);
            UserNotifyDto dto = UserNotifyDto.builder()
                    .actionUserId(senderUser.getId())
                    .actionUserCode(senderUser.getUserCode())
                    .actionUsername(senderUser.getFullName())
                    .actionUserAvatar(senderUser.getAvatar())
                    .build();
            NotificationData notifyData = entityToNotifyData(notify, dto);
            SocketBody<?> notifySocket = SocketBody.builder()
                    .type(SocketBody.Type.NOTIFY)
                    .body(notifyData).createdAt(null).build();
            messagingTemplate.convertAndSendToUser(userCode, "/queue/private", notifySocket);
        } catch (Exception e){
            log.error("Send notification to User {} error!", userCode);
            throw new RequestNotFoundException("Error");
        }
    }

    @Override
    public void sendSystemNotifyToUser(String userCode, String content, LocalDateTime createdAt) {
        try{
            Users user = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(() -> new RequestNotFoundException("User not found!"));

            Notifications notify = new Notifications();
            notify.setType(Notifications.Type.SYSTEM);
            notify.setContent(content);
            notify.setIsRead(false);
            notify.setUser(user);
            notify.setCreatedAt(createdAt);
            notify.setUpdatedAt(createdAt);
            notify = notifyRepository.save(notify);
            NotificationData notifyData = entityToNotifyData(notify, null);
            SocketBody<?> notifySocket = SocketBody.builder()
                    .type(SocketBody.Type.NOTIFY)
                    .body(notifyData).createdAt(null).build();
            messagingTemplate.convertAndSendToUser(userCode, "/queue/private", notifySocket);
        } catch (Exception e){
            log.error("Send notification to User {} error!", userCode);
            throw new RequestNotFoundException("Error");
        }
    }

    @Override
    public void sendPostNotifyToUser(Notifications.Type type,
                                     String toUserCode,
                                     Long postId,
                                     PostNotifyDto postNotifyDto,
                                     LocalDateTime createdAt) {
        try{
            Users user = usersRepository.findUsersByUserCode(toUserCode)
                    .orElseThrow(() -> new RequestNotFoundException("User not found!"));
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RequestNotFoundException("Post not found!"));
            Notifications notify = new Notifications();
            if(type==Notifications.Type.REACTION_POST){
                notify.setType(Notifications.Type.REACTION_POST);
                notify.setContent(" đã bày tỏ cảm xúc về bài viết của bạn.");
            } else if(type==Notifications.Type.COMMENT_POST){
                notify.setType(Notifications.Type.COMMENT_POST);
                notify.setContent(" đã binh luận về bài viết của bạn.");
            } else if(type==Notifications.Type.SHARE_POST){
                notify.setType(Notifications.Type.SHARE_POST);
                notify.setContent(" đã chia sẻ bài viết của bạn.");
            } else{
                throw new RequestNotFoundException("Error");
            }
            notify.setIsRead(false);
            notify.setUser(user);
            notify.setPost(post);
            notify.setReaction(postNotifyDto.getMyReaction()!=null ? postNotifyDto.getMyReaction() : null);
            notify.setCreatedAt(createdAt);
            notify.setUpdatedAt(createdAt);
            notify = notifyRepository.save(notify);
            NotificationData notifyData = entityToNotifyData(notify, postNotifyDto);
            SocketBody<?> notifySocket = SocketBody.builder()
                    .type(SocketBody.Type.NOTIFY)
                    .body(notifyData).createdAt(null).build();
            messagingTemplate.convertAndSendToUser(toUserCode, "/queue/private", notifySocket);
        } catch (Exception e){
            log.error("Send notification to User {} error!", toUserCode);
            throw new RequestNotFoundException("Error");
        }
    }

    @Override
    public void sendPostCommentNotifyToUser(Notifications.Type type,
                                            String toUserCode,
                                            Long commentId,
                                            PostCommentNotifyDto postCommentNotifyDto,
                                            LocalDateTime createdAt) {
        try{
            Users user = usersRepository.findUsersByUserCode(toUserCode)
                    .orElseThrow(() -> new RequestNotFoundException("User not found!"));
            PostComment postComment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RequestNotFoundException("Comment not found!"));
            Notifications notify = new Notifications();
            if(type==Notifications.Type.REACTION_COMMENT){
                notify.setType(Notifications.Type.REACTION_COMMENT);
                notify.setContent(" đã bày tỏ cảm xúc về bình luận của bạn.");
            } else if(type==Notifications.Type.REPLY_COMMENT){
                notify.setType(Notifications.Type.REPLY_COMMENT);
                notify.setContent(" đã trả lời một bình luận của bạn.");
            } else{
                throw new RequestNotFoundException("Error");
            }
            notify.setIsRead(false);
            notify.setUser(user);
            notify.setPostComment(postComment);
            notify.setReaction(postCommentNotifyDto.getReaction()!=null ? postCommentNotifyDto.getReaction() : null);
            notify.setCreatedAt(createdAt);
            notify.setUpdatedAt(createdAt);
            notify = notifyRepository.save(notify);
            NotificationData notifyData = entityToNotifyData(notify, postCommentNotifyDto);
            SocketBody<?> notifySocket = SocketBody.builder()
                    .type(SocketBody.Type.NOTIFY)
                    .body(notifyData).createdAt(null).build();
            messagingTemplate.convertAndSendToUser(toUserCode, "/queue/private", notifySocket);
        } catch (Exception e){
            log.error("Send notification to User {} error!", toUserCode);
            throw new RequestNotFoundException("Error");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userGetNotifyList(int pageNumber, int pageSize) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Page<Notifications> page = notifyRepository.getNotificationOfUser(userId,
                    PageRequest.of(pageNumber-1, pageSize, Sort.by(Sort.Order.desc("createdAt"))));
            PageData<?> pageData = PageData.builder()
                    .pageSize(pageSize)
                    .pageNumber(pageNumber)
                    .totalPage(page.getTotalPages())
                    .totalData(page.getTotalElements())
                    .data(page.stream().map(this::mapNotifyToData).collect(Collectors.toList()))
                    .build();
            return new ResponseBody<>(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Get notification Error!");
            throw new RequestNotFoundException("Error");
        }
    }

    public NotificationData mapNotifyToData(Notifications notify){
        if(notify.getType()==Notifications.Type.REACTION_POST
                || notify.getType()==Notifications.Type.COMMENT_POST
                || notify.getType()==Notifications.Type.SHARE_POST){
            Post post = notify.getPost();
            Users user = post.getUsers();
            PostNotifyDto postNotifyDto = PostNotifyDto.builder()
                    .postId(post.getId())
                    .postContent(post.getContent())
                    .myComment(null)
                    .myReaction(notify.getReaction())
                    .actionUserCode(user.getUserCode())
                    .actionUsername(user.getFullName())
                    .actionUserAvatar(user.getAvatar())
                    .build();
            return entityToNotifyData(notify, postNotifyDto);
        } else if(notify.getType()==Notifications.Type.FRIEND_REQUEST
                || notify.getType()==Notifications.Type.FRIEND_ACCEPTED
                || notify.getType()==Notifications.Type.FOLLOW_REQUEST){
            Users user = notify.getSenderUser();
            UserNotifyDto userNotifyDto = UserNotifyDto.builder()
                    .actionUserId(user.getId())
                    .actionUserCode(user.getUserCode())
                    .actionUsername(user.getFullName())
                    .actionUserAvatar(user.getAvatar())
                    .build();
            return entityToNotifyData(notify, userNotifyDto);
        } else if(notify.getType()==Notifications.Type.REACTION_COMMENT
                || notify.getType()==Notifications.Type.REPLY_COMMENT){
            PostComment postComment = notify.getPostComment();
            Users user = postComment.getUsers();
            PostCommentNotifyDto postCommentNotifyDto = PostCommentNotifyDto.builder()
                    .actionUserCode(user.getUserCode())
                    .actionUsername(user.getFullName())
                    .actionUserAvatar(user.getAvatar())
                    .commentId(postComment.getId())
                    .commentContent(postComment.getContent())
                    .reaction(notify.getReaction())
                    .build();
            return entityToNotifyData(notify, postCommentNotifyDto);
        } else {
            return entityToNotifyData(notify, null);
        }
    }

    @Override
    public ResponseBody<?> userGetInfoData() {
        return null;
    }

}
