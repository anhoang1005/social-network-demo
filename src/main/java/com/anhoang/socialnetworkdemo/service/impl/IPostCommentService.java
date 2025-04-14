package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.entity.*;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.exceptions.users.UnauthorizedException;
import com.anhoang.socialnetworkdemo.mapper.PostCommentMapper;
import com.anhoang.socialnetworkdemo.model.notify.PostCommentNotifyDto;
import com.anhoang.socialnetworkdemo.model.notify.PostNotifyDto;
import com.anhoang.socialnetworkdemo.model.post.post_comment.PostCommentDto;
import com.anhoang.socialnetworkdemo.model.post.post_comment.PostCommentRequest;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.repository.PostCommentRepository;
import com.anhoang.socialnetworkdemo.repository.PostReactionRepository;
import com.anhoang.socialnetworkdemo.repository.PostRepository;
import com.anhoang.socialnetworkdemo.repository.UsersRepository;
import com.anhoang.socialnetworkdemo.service.FileService;
import com.anhoang.socialnetworkdemo.service.NotificationService;
import com.anhoang.socialnetworkdemo.service.PostCommentService;
import com.anhoang.socialnetworkdemo.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IPostCommentService implements PostCommentService {
    private final PostCommentRepository pcmRepository;
    private final AuthenticationUtils authUtils;
    private final PostRepository postRepository;
    private final UsersRepository usersRepository;
    private final FileService fileService;
    private final PostCommentMapper commentMapper;
    private final PostReactionRepository postReactionRepository;
    private final NotificationService notifyService;


    @Override
    @Transactional
    public ResponseBody<?> userGetListComment(Long postId, Long commentId, int pageNumber) {
        try{
            Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by(Sort.Order.asc("createdAt")));
            if(postId!=null && commentId==null){
                Page<PostComment> page = pcmRepository.findPostCommentByPostIdOrCommentId(postId, commentId, 0, pageable);
                List<PostCommentDto> dtoList = page.stream()
                        .map(comment -> commentMapper.entityToDto(comment, false)).collect(Collectors.toList());
                return new ResponseBody<>(dtoList, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else {
                Page<PostComment> page = pcmRepository.findPostCommentByPostIdOrCommentId(postId, commentId, null, pageable);
                List<PostCommentDto> dtoList = page.stream()
                        .map(comment -> commentMapper.entityToDto(comment, false)).collect(Collectors.toList());
                return new ResponseBody<>(dtoList, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e){
            log.error("get list comment error! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userCreateComment(PostCommentRequest dto, MultipartFile file) {
        try{
            PostComment comment = new PostComment();
            comment.setContent(dto.getContent());
            Post post = postRepository.findById(dto.getPostId())
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            comment.setPost(post);
            if(file!=null && !file.isEmpty()){
                ResponseBody<?> fileResp = fileService.uploadToCloudinary(file);
                comment.setMediaUrl(fileResp.getData().toString());
            }
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Users users = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            comment.setUsers(users);
            if(dto.getParentId()!=null){
                PostComment parent = pcmRepository.findById(dto.getParentId())
                        .orElseThrow(()-> new RequestNotFoundException("ERROR"));
                comment.setCommentParent(parent);
                comment.setLever(parent.getLever() + 1);
                comment = pcmRepository.save(comment);
                pcmRepository.incrementChildrenCount(parent.getId());

                String toUserCode = comment.getUsers().getUserCode();
                if(!toUserCode.equals(authUtils.getUserFromAuthentication().getUserCode())){
                    PostCommentNotifyDto postCommentNotifyDto = PostCommentNotifyDto.builder()
                            .commentId(comment.getId())
                            .commentContent(comment.getContent())
                            .reaction(null)
                            .actionUserCode(users.getUserCode())
                            .actionUsername(users.getFullName())
                            .actionUserAvatar(users.getAvatar())
                            .build();
                    notifyService.sendPostCommentNotifyToUser(Notifications.Type.REPLY_COMMENT,
                            comment.getUsers().getUserCode(),
                            comment.getId(), postCommentNotifyDto, comment.getUpdatedAt());
                }
            } else {
                comment.setCommentParent(null);
                comment.setLever(0);
                comment = pcmRepository.save(comment);

                String toUserCode = post.getUsers().getUserCode();
                if(!toUserCode.equals(userCode)){
                    PostNotifyDto postNotifyDto = PostNotifyDto.builder()
                            .postId(post.getId()).postContent(post.getContent())
                            .myReaction(null).myComment(comment.getContent())
                            .actionUserCode(userCode)
                            .actionUsername(users.getFullName())
                            .actionUserAvatar(users.getAvatar())
                            .build();
                    notifyService.sendPostNotifyToUser(Notifications.Type.COMMENT_POST,
                            post.getUsers().getUserCode(), post.getId(),
                            postNotifyDto, comment.getUpdatedAt());
                }
            }
            return new ResponseBody<>(commentMapper.entityToDto(comment, false), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("create comment error! Error: " + e.getMessage());
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userUpdateComment(PostCommentRequest dto, MultipartFile file) {
        try{
            PostComment comment = pcmRepository.findById(dto.getCommentId())
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            comment.setContent(dto.getContent());
            if(file!=null && !file.isEmpty()){
                ResponseBody<?> fileResp = fileService.uploadToCloudinary(file);
                comment.setMediaUrl(fileResp.getData().toString());
            }
            comment = pcmRepository.save(comment);
            return new ResponseBody<>(commentMapper.entityToDto(comment, false), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        }catch (Exception e){
            log.error("update comment error! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userDeleteComment(Long commentId) {
        try{
            String email = authUtils.getUserFromAuthentication().getEmail();
            PostComment comment = pcmRepository.findById(commentId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            if(!comment.getCreatedBy().equals(email)){
                throw new UnauthorizedException("ERROR");
            } else {
                pcmRepository.delete(comment);
                return new ResponseBody<>("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e){
            log.error("delete comment error! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userReactionComment(Long commentId, PostReaction.Reaction reaction) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Users users = usersRepository.findById(userId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            PostComment comment = pcmRepository.findById(commentId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            PostReaction postReaction = postReactionRepository.findPostReactionByCommentIdAndUserId(userId, commentId);
            if(postReaction!=null){
                if(reaction.equals(PostReaction.Reaction.NONE)){
                    postReactionRepository.delete(postReaction);
                    return new ResponseBody<>("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
                }
                postReaction.setReaction(reaction);
                postReactionRepository.save(postReaction);
            } else {
                if (reaction.equals(PostReaction.Reaction.NONE)){
                    return new ResponseBody<>("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
                }
                postReaction = new PostReaction();
                postReaction.setUsers(users);
                postReaction.setReaction(reaction);
                postReaction.setPostComment(comment);
                postReaction = postReactionRepository.save(postReaction);

                String toUserCode = comment.getUsers().getUserCode();
                if(!toUserCode.equals(userCode)){
                    PostCommentNotifyDto postCommentNotifyDto = PostCommentNotifyDto.builder()
                            .commentId(comment.getId())
                            .commentContent(comment.getContent())
                            .reaction(reaction)
                            .actionUserCode(users.getUserCode())
                            .actionUsername(users.getFullName())
                            .actionUserAvatar(users.getAvatar())
                            .build();
                    notifyService.sendPostCommentNotifyToUser(Notifications.Type.REPLY_COMMENT, comment.getUsers().getUserCode(),
                            commentId, postCommentNotifyDto, postReaction.getUpdatedAt());
                }
            }
            return new ResponseBody<>("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("reaction comment error! Error: " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }
}
