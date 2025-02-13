package com.anhoang.socialnetworkdemo.model.post.post_comment;

import com.anhoang.socialnetworkdemo.entity.Roles;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentRequest {
    private Long commentId;
    private Long postId;
    private Long parentId;
    private String content;
}
