package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.entity.Roles;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.mapper.UsersMapper;
import com.anhoang.socialnetworkdemo.model.users.UserRegisterRequest;
import com.anhoang.socialnetworkdemo.payload.PageData;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.repository.RolesRepository;
import com.anhoang.socialnetworkdemo.repository.UsersRepository;
import com.anhoang.socialnetworkdemo.service.FileService;
import com.anhoang.socialnetworkdemo.service.UsersService;
import com.anhoang.socialnetworkdemo.utils.AuthenticationUtils;
import com.anhoang.socialnetworkdemo.utils.TimeMapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class IUsersService implements UsersService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationUtils authenticationUtils;
    private final FileService fileService;
    private final RolesRepository rolesRepository;

    @Override
    @Transactional
    public ResponseBody<?> usersChangePassword(String oldPassword, String newPassword) {
        String userCode = authenticationUtils.getUserFromAuthentication().getUserCode();
        Users users = usersRepository.findUsersByUserCode(userCode)
                .orElseThrow(() -> new RequestNotFoundException("ERROR"));
        if(passwordEncoder.matches(oldPassword, users.getPassword())){
            users.setPassword(passwordEncoder.encode(newPassword));
            usersRepository.save(users);
            return new ResponseBody<>("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } else{
            return new ResponseBody<>("", ResponseBody.Status.SUCCESS, "INCORRECT_PASSWORD", ResponseBody.Code.SUCCESS);
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> usersChangeUserInfo(UserRegisterRequest info) {
        try{
            String userCode = authenticationUtils.getUserFromAuthentication().getUserCode();
            Users user = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(() -> new RequestNotFoundException("ERROR"));
            user.setPhoneNumber(info.getPhoneNumber());
            user.setDob(TimeMapperUtils.stringToLocalDate(info.getDob()));
            user.setFullName(info.getFullName());
            user.setGender(info.getGender());
            user.setBio(info.getBio());
            usersRepository.save(user);
            return new ResponseBody<>("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Change info error! Error " + e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    @Transactional
    public ResponseBody<?> usersChangeAvatar(MultipartFile file) {
        String userCode = authenticationUtils.getUserFromAuthentication().getUserCode();
        Users user = usersRepository.findUsersByUserCode(userCode)
                .orElseThrow(() -> new RequestNotFoundException("ERROR"));
        ResponseBody<?> responseImage = fileService.uploadToCloudinary(file);
        user.setAvatar(responseImage.getData().toString());
        usersRepository.save(user);
        return new ResponseBody<>(responseImage.getData().toString(), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
    }

    @Override
    public ResponseBody<?> usersGetUsersDetailResponse() {
        String userCode = authenticationUtils.getUserFromAuthentication().getUserCode();
        Users usersEntity = usersRepository.findUsersByUserCode(userCode)
                .orElseThrow(()-> new RequestNotFoundException("ERROR"));
        return new ResponseBody<>(
                usersMapper.entityToUsersDetailResponse(usersEntity),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        );
    }

    @Transactional
    @Override
    public ResponseBody<?> rootChangeRoleUsers(String userCode, boolean isAdmin) {
        try {
            Users users = usersRepository.findUsersByUserCode(userCode)
                    .orElseThrow(()-> new RequestNotFoundException("ERROR"));
            List<Roles> rolesUsers = users.getRolesList();

            boolean hasQuanLiRole = rolesUsers.stream().anyMatch(role -> role.getRoleName()==Roles.BaseRole.ADMIN);

            if (isAdmin && !hasQuanLiRole) {
                rolesRepository.findRolesByRoleName(Roles.BaseRole.ADMIN)
                        .ifPresent(rolesUsers::add);
            } else if (!isAdmin && hasQuanLiRole) {
                rolesUsers.removeIf(role -> role.getRoleName()== Roles.BaseRole.ADMIN);
            }
            users.setRolesList(rolesUsers);
            usersRepository.save(users);
            log.info("Root users change users {} to admin success!", userCode);
            return new ResponseBody<>("", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e) {
            log.error("Root users change users to {} admin failed! Error: {}", userCode, e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody<?> getListUsersAccount(int pageNumber, int pageSize) {
        try{
            Long userId = authenticationUtils.getUserFromAuthentication().getId();
            Page<Users> page = usersRepository.getAllByIdAndStatus(userId,
                    Users.Status.BINH_THUONG, PageRequest.of(pageNumber-1, pageSize));
            PageData<?> pageData = PageData.builder()
                    .pageNumber(page.getNumber() + 1)
                    .pageSize(page.getSize())
                    .totalData(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .data(page.stream()
                            .map(users -> usersMapper.entityToUserShortDto(userId, users)).collect(Collectors.toList()))
                    .build();
            return new ResponseBody<>(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Get list users account error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }

    @Override
    public ResponseBody<?> getListUsersAccountByFullName(String fullName, int pageNumber, int pageSize) {
        try{
            Long userId = authenticationUtils.getUserFromAuthentication().getId();
            Page<Users> page = usersRepository.findUsersByFullNameLikeAndStatus(fullName, Users.Status.BINH_THUONG, PageRequest.of(pageNumber-1 ,pageSize));
            PageData<?> pageData = PageData.builder()
                    .pageNumber(page.getNumber() + 1)
                    .pageSize(page.getSize())
                    .totalData(page.getTotalElements())
                    .totalPage(page.getTotalPages())
                    .data(page.stream()
                            .map(users -> usersMapper.entityToUserShortDto(userId, users)).collect(Collectors.toList()))
                    .build();
            return new ResponseBody<>(pageData, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Get list users account error! Error: {}", e.getMessage());
            throw new RequestNotFoundException("ERROR");
        }
    }
}
