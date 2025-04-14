package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.entity.Friendship;
import com.anhoang.socialnetworkdemo.entity.Notifications;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.mapper.UsersMapper;
import com.anhoang.socialnetworkdemo.payload.PageData;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.repository.FriendshipRepository;
import com.anhoang.socialnetworkdemo.service.FriendshipService;
import com.anhoang.socialnetworkdemo.service.NotificationService;
import com.anhoang.socialnetworkdemo.utils.AuthenticationUtils;
import com.anhoang.socialnetworkdemo.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IFriendshipService implements FriendshipService{
    private final AuthenticationUtils authUtils;
    private final FriendshipRepository friendshipRepository;
    private final UsersRepository usersRepository;
    private final NotificationService notifyService;
    private final UsersMapper usersMapper;

    @Override
    @Transactional
    public Boolean checkIsFriend(Long userOtherId) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users users = usersRepository.findById(userId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            Users friend = usersRepository.findById(userOtherId)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            Boolean isFriend = friendshipRepository.existsByUserAndFriendAndStatus(users, friend, Friendship.FriendshipStatus.ACCEPTED);
            return isFriend != null && isFriend;
        } catch (Exception e){
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userFriendshipActionEvent(String type, Long userOtherId) {
        try {
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users me = usersRepository.findById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("User not found"));
            Users friend = usersRepository.findById(userOtherId)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
            switch (type) {
                case "ACCEPT_REQUEST" -> {
                    //Cap nhat ban ghi cu
                    Friendship friendship = friendshipRepository.findByUserAndFriendAndStatus(
                            friend, me, Friendship.FriendshipStatus.PENDING)
                            .orElseThrow(() -> new RequestNotFoundException("Friend request not found"));
                    friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
                    friendshipRepository.save(friendship);
                    //Tao ban ghi moi
                    Friendship friendship1 = new Friendship();
                    friendship1.setUser(me);
                    friendship1.setFriend(friend);
                    friendship1.setStatus(Friendship.FriendshipStatus.ACCEPTED);
                    friendship1 = friendshipRepository.save(friendship1);

                    notifyService.sendFriendshipNotifyToUser(Notifications.Type.FRIEND_ACCEPTED, me.getUserCode(),
                            friend.getUserCode(), friendship1.getUpdatedAt());

                    return new ResponseBody<>("OK");
                }
                case "SEND_REQUEST" -> {
                    Friendship friendship = new Friendship();
                    friendship.setUser(me);
                    friendship.setFriend(friend);
                    friendship.setStatus(Friendship.FriendshipStatus.PENDING);
                    friendship = friendshipRepository.save(friendship);
                    notifyService.sendFriendshipNotifyToUser(Notifications.Type.FRIEND_REQUEST, me.getUserCode(),
                            friend.getUserCode(), friendship.getUpdatedAt());
                    return new ResponseBody<>("OK");
                }
                case "REMOVE_REQUEST" -> {
                    friendshipRepository.deleteFriendShip(userId, userOtherId, Friendship.FriendshipStatus.PENDING);
                    return new ResponseBody<>("OK");
                }
                case "REJECT_REQUEST" -> {
                    friendshipRepository.deleteFriendShip(userOtherId, userId, Friendship.FriendshipStatus.PENDING);
                    return new ResponseBody<>("OK");
                }
                case "UNFRIEND" -> {
                    friendshipRepository.deleteFriendShip(userId, userOtherId, Friendship.FriendshipStatus.ACCEPTED);
                    friendshipRepository.deleteFriendShip(userOtherId, userId, Friendship.FriendshipStatus.ACCEPTED);
                    return new ResponseBody<>("OK");
                }
                case "BLOCK" -> {
                    Friendship friendship = friendshipRepository.findByUserAndFriendAndStatus(
                            me, friend, Friendship.FriendshipStatus.ACCEPTED)
                            .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
                    friendship.setStatus(Friendship.FriendshipStatus.BLOCKED);
                    friendshipRepository.save(friendship);
                    Friendship friendship1 = friendshipRepository.findByUserAndFriendAndStatus(
                            friend, me, Friendship.FriendshipStatus.ACCEPTED)
                            .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
                    friendship1.setStatus(Friendship.FriendshipStatus.BLOCKED);
                    friendshipRepository.save(friendship1);
                    return new ResponseBody<>("OK");
                }
                case "UNBLOCK" -> {
                    Friendship friendship = friendshipRepository.findByUserAndFriendAndStatus(
                            me, friend, Friendship.FriendshipStatus.BLOCKED)
                            .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
                    friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
                    friendshipRepository.save(friendship);
                    Friendship friendship1 = friendshipRepository.findByUserAndFriendAndStatus(
                            friend, me, Friendship.FriendshipStatus.BLOCKED)
                            .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
                    friendship1.setStatus(Friendship.FriendshipStatus.ACCEPTED);
                    friendshipRepository.save(friendship1);
                    return new ResponseBody<>("OK");
                }
                default -> { return new ResponseBody<>("", ResponseBody.Status.SUCCESS, ResponseBody.Code.FORBIDDEN); }
            }
        } catch (Exception e){
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> getFriendshipList(String type, Long userOtherId, int pageNumber, int pageSize) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Page<Users> page = switch (type) {
                case "FRIEND" -> friendshipRepository.getAllFriendsOfMe(userId,
                        PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("id"))));
                case "PENDING_BY_ME" -> friendshipRepository.getFriendRequestSendByMe(userId,
                        PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("id"))));
                case "PENDING_TO_ME" -> friendshipRepository.getFriendRequestInviteMe(userId,
                        PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("id"))));
                case "NONE_SUGGEST" -> friendshipRepository.getUsersNotInFriendship(userId,
                        PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("id"))));
                case "MUTUAL_FRIEND" -> friendshipRepository.getListMutualFriends(userId, userOtherId,
                        PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("id"))));
                case "BLOCK" -> friendshipRepository.getListUserBlock(userId, PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Order.desc("id"))));
                default -> throw new RequestNotFoundException("Error! Type not found");
            };
            PageData<?> pageData = PageData.builder()
                    .totalData(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .pageNumber(page.getNumber() + 1)
                    .pageSize(page.getSize())
                    .data(page.stream()
                            .map(users -> usersMapper.entityToUserShortDto(userId, users)).collect(Collectors.toList()))
                    .build();
            return new ResponseBody<>(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Long getFriendshipCount(String type, Long userOtherId) {
        try {
            if(type.equals("FRIEND")){
                Long userId = authUtils.getUserFromAuthentication().getId();
                Long friendCount = friendshipRepository.countFriendOfUser(userId);
                return friendCount != null ? friendCount : 0L;
            } else if(type.equals("MUTUAL_FRIEND")){
                Long userId = authUtils.getUserFromAuthentication().getId();
                Long userCount = friendshipRepository.countMutualFriends(userId, userOtherId);
                return userCount != null ? userCount : 0L;
            } else{
                throw new RequestNotFoundException("Error: Type not found!");
            }
        } catch (Exception e){
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }
}
