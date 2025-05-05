package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.config.Constant;
import com.anhoang.socialnetworkdemo.entity.Group;
import com.anhoang.socialnetworkdemo.entity.GroupMember;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.mapper.GroupMapper;
import com.anhoang.socialnetworkdemo.model.group.CreateGroupRequest;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.repository.GroupMemberRepository;
import com.anhoang.socialnetworkdemo.repository.GroupRepository;
import com.anhoang.socialnetworkdemo.repository.UsersRepository;
import com.anhoang.socialnetworkdemo.service.GroupService;
import com.anhoang.socialnetworkdemo.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class IGroupService implements GroupService {
    private final AuthenticationUtils authUtils;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UsersRepository usersRepository;
    private final GroupMapper groupMapper;


    @Override
    public ResponseBody<?> userCreateGroup(CreateGroupRequest request) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Users user = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(() -> new RequestNotFoundException("user not found!"));
            Group group = new Group();
            group.setGroupAvatar(Constant.BAC_IMAGE);
            group.setGroupName(request.getGroupName());
            group.setDescription(request.getGroupDes());
            group.setIsSecretGroup(request.getIsSecretGroup());
            group.setGroupType(request.getGroupType());
            group.setRequiresApproval(request.isRequiresApproval());
            group.setAllowMembersToPost(request.isAllowMembersToPost());
            group.setAllowComments(request.isAllowComments());
            group.setAllowNotifications(request.isAllowNotifications());
            group.setGroupStatus(Group.GroupStatus.NORMAL);

            GroupMember rootMember = new GroupMember();
            rootMember.setGroup(group);
            rootMember.setUser(user);
            rootMember.setRole(GroupMember.Role.ADMIN);
            rootMember.setStatus(GroupMember.MemberStatus.APPROVED);
            rootMember.setJoinedAt(LocalDateTime.now());
            group.setMembers(List.of(rootMember));
            group = groupRepository.save(group);
            return new ResponseBody<>(groupMapper.entityToGroupDto(group),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("create group error!");
            throw new RequestNotFoundException("Error");
        }
    }

    @Override
    public ResponseBody<?> userUpdateGroup(CreateGroupRequest request) {
        try{
            String userCode = authUtils.getUserFromAuthentication().getUserCode();
            Group group = new Group();
            group.setGroupAvatar(Constant.BAC_IMAGE);
            group.setGroupName(request.getGroupName());
            group.setDescription(request.getGroupDes());
            group.setIsSecretGroup(request.getIsSecretGroup());
            group.setGroupType(request.getGroupType());
            group.setRequiresApproval(request.isRequiresApproval());
            group.setAllowMembersToPost(request.isAllowMembersToPost());
            group.setAllowComments(request.isAllowComments());
            group.setAllowNotifications(request.isAllowNotifications());
            group.setGroupStatus(Group.GroupStatus.NORMAL);
            group = groupRepository.save(group);
            return new ResponseBody<>(groupMapper.entityToGroupDto(group),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("create group error!");
            throw new RequestNotFoundException("Error");
        }
    }

    @Override
    public ResponseBody<?> userDeleteGroup(Long groupId) {
        try{
            Long userId  = authUtils.getUserFromAuthentication().getId();
            GroupMember admin = groupMemberRepository.searchGroupByRoleAndUserId(groupId, userId, GroupMember.Role.ADMIN)
                    .orElseThrow(()-> new RequestNotFoundException("member not found!"));
            Group group = admin.getGroup();
            group.setGroupStatus(Group.GroupStatus.DELETED);
            group = groupRepository.save(group);
            return new ResponseBody<>(groupMapper.entityToGroupDto(group),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("create group error!");
            throw new RequestNotFoundException("Error");
        }
    }

    @Override
    public ResponseBody<?> userViewGroup(Long groupId) {
        try{
            Long userId  = authUtils.getUserFromAuthentication().getId();
            GroupMember member = groupMemberRepository.searchGroupByRoleAndUserId(groupId, userId, null)
                    .orElseThrow(()-> new RequestNotFoundException("member not found!"));
            Group group = member.getGroup();
            return new ResponseBody<>(groupMapper.entityToGroupDto(group),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("create group error!");
            throw new RequestNotFoundException("Error");
        }
    }

    @Override
    public ResponseEntity<?> adminAcceptMember() {
        return null;
    }

    @Override
    public ResponseEntity<?> userCreateNewPostGroup() {
        return null;
    }

    @Override
    public ResponseEntity<?> adminAcceptNewPostGroup() {
        return null;
    }
}
