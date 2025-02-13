package com.anhoang.socialnetworkdemo.mapper;

import com.anhoang.socialnetworkdemo.entity.PostComment;
import com.anhoang.socialnetworkdemo.entity.Users;
import com.anhoang.socialnetworkdemo.model.post.post_comment.PostCommentDto;
import com.anhoang.socialnetworkdemo.utils.TimeMapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PostCommentMapper {

    public PostCommentDto entityToDto(PostComment comment, boolean children){
        PostCommentDto dto = new PostCommentDto();
        Users users = comment.getUsers();
        dto.setUserCode(users.getUserCode());
        dto.setUserAvatar(users.getAvatar());
        dto.setUsername(users.getFullName());
        dto.setCommentId(comment.getId());
        dto.setLever(comment.getLever());
        dto.setMediaUrl(comment.getMediaUrl());
        dto.setContent(comment.getContent());
        dto.setChildrenCount(comment.getChildrenCommentList() != null ? comment.getChildrenCommentList().size() : 0);
        dto.setCreatedAt(TimeMapperUtils.formatFacebookTime(comment.getCreatedAt()));
        if(children){
            dto.setChildren(getChildrenOfComment(comment));
        } else{
            dto.setChildren(null);
        }
        return dto;
    }

    public List<PostCommentDto> getChildrenOfComment(PostComment comment){
        List<PostComment> children = comment.getChildrenCommentList();
        if(children!=null && !children.isEmpty()){
            return children.stream()
                    .map(cm -> entityToDto(comment, false)).collect(Collectors.toList());
        } else{
            return null;
        }
    }
}
