package com.anhoang.socialnetworkdemo.model.post;

import com.anhoang.socialnetworkdemo.entity.Post;
import com.anhoang.socialnetworkdemo.entity.Roles;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    private Long id;
    private Long sharedId;
    private Post.Visibility visibility;
    private String content;
    private String location;
    private List<String> hashTag;
}
