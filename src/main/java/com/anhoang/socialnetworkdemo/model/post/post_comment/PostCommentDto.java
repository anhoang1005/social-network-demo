package com.anhoang.socialnetworkdemo.model.post.post_comment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentDto {
    private Long commentId;
    private String userCode;
    private String shopCode;
    private String userAvatar;
    private String username;
    private String content;
    private String mediaUrl;
    private Integer lever;
    private String createdAt;
    private Integer childrenCount;
    private List<PostCommentDto> children;
}
