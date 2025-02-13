package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.entity.Friendship;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.mapper.UsersMapper;
import com.anhoang.socialnetworkdemo.model.users.UserShortDto;
import com.anhoang.socialnetworkdemo.payload.PageData;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.repository.FriendshipRepository;
import com.anhoang.socialnetworkdemo.repository.UsersRepository;
import com.anhoang.socialnetworkdemo.service.FriendshipService;
import com.anhoang.socialnetworkdemo.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class IFriendshipService implements FriendshipService{
    private final AuthenticationUtils authUtils;
    private final FriendshipRepository friendshipRepository;
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    @Override
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
    public ResponseBody<?> userSendFriendRequest(String userCode) {
        try {
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users me = usersRepository.findById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("User not found"));
            Users friend = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
            Friendship friendship = new Friendship();
            friendship.setUser(me);
            friendship.setFriend(friend);
            friendship.setStatus(Friendship.FriendshipStatus.PENDING);
            friendshipRepository.save(friendship);
            return new ResponseBody<>("OK");
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> userAcceptFriendRequest(String userCode) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users me = usersRepository.findById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("User not found"));
            Users friend = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
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
            friendshipRepository.save(friendship1);
            return new ResponseBody<>("OK");
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseBody<?> userRemoveFriend(String userCode) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users me = usersRepository.findById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("User not found"));
            Users friend = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
            Friendship friendship = friendshipRepository.findByUserAndFriendAndStatus(
                    me, friend, Friendship.FriendshipStatus.ACCEPTED)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
            friendshipRepository.delete(friendship);
            Friendship friendship1 = friendshipRepository.findByUserAndFriendAndStatus(
                    friend, me, Friendship.FriendshipStatus.ACCEPTED)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
            friendshipRepository.delete(friendship1);
            return new ResponseBody<>("OK");
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseBody<?> userDeclineFriendRequest(String userCode) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users me = usersRepository.findById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("User not found"));
            Users friend = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
            Friendship friendship = friendshipRepository.findByUserAndFriendAndStatus(
                    friend, me, Friendship.FriendshipStatus.PENDING)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
            friendshipRepository.delete(friendship);
            return new ResponseBody<>("OK");
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseBody<?> userRemoveFriendRequest(String userCode) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users me = usersRepository.findById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("User not found"));
            Users friend = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
            Friendship friendship = friendshipRepository.findByUserAndFriendAndStatus(
                    me, friend, Friendship.FriendshipStatus.PENDING)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
            friendshipRepository.delete(friendship);
            return new ResponseBody<>("OK");
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseBody<?> userBlockOrUnlockFriend(String userCode, boolean isBlock) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Users me = usersRepository.findById(userId)
                    .orElseThrow(() -> new RequestNotFoundException("User not found"));
            Users friend = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
            Friendship friendship = friendshipRepository.findByUserAndFriendAndStatus(
                    me, friend, Friendship.FriendshipStatus.ACCEPTED)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
            if(isBlock){
                friendship.setStatus(Friendship.FriendshipStatus.BLOCKED);
            } else {
                friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
            }
            friendshipRepository.save(friendship);
            Friendship friendship1 = friendshipRepository.findByUserAndFriendAndStatus(
                    friend, me, Friendship.FriendshipStatus.ACCEPTED)
                    .orElseThrow(() -> new RequestNotFoundException("Friend not found"));
            if(isBlock){
                friendship1.setStatus(Friendship.FriendshipStatus.BLOCKED);
            } else {
                friendship1.setStatus(Friendship.FriendshipStatus.ACCEPTED);
            }
            friendshipRepository.save(friendship1);
            return new ResponseBody<>("OK");
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseBody<?> getFriendsList(int pageNumber, int pageSize) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Page<Users> page = friendshipRepository.findAllFriends(
                    userId, PageRequest.of(pageNumber-1, pageSize));
            List<UserShortDto> friends = page.stream().map(usersMapper::entityToUserShortDto).collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .data(friends)
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .totalData(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .build();
            return new ResponseBody<>(friends);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public Long getFriendListCount() {
        try {
            Long userId = authUtils.getUserFromAuthentication().getId();
            Long friendCount = friendshipRepository.countFriendOfUser(userId);
            return friendCount!=null ? friendCount : 0L;
        } catch (Exception e){
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public ResponseBody<?> getMutualFriendsList(Long userOtherId, int pageNumber, int pageSize) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Page<Users> page = friendshipRepository.findMutualFriendsByPage(
                    userId, userOtherId, PageRequest.of(pageNumber - 1, pageSize));
            List<UserShortDto> userShortDtoList = page.stream().map(usersMapper::entityToUserShortDto).collect(Collectors.toList());
            PageData<?> pageData = PageData.builder()
                    .data(userShortDtoList)
                    .totalPage(page.getTotalPages())
                    .totalData(page.getTotalElements())
                    .pageSize(pageSize)
                    .pageNumber(pageNumber)
                    .build();
            return new ResponseBody<>(pageData);
        } catch (Exception e){
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public Long getMutualFriendCount(Long userOtherId) {
        try{
            Long userId = authUtils.getUserFromAuthentication().getId();
            Long userCount = friendshipRepository.countMutualFriends(userId, userOtherId);
            return userCount!=null ? userCount : 0L;
        } catch (Exception e){
            log.error("Error: " + e.getMessage());
            throw new RequestNotFoundException("Error: " + e.getMessage());
        }
    }
}
