package com.anhoang.socialnetworkdemo.model.post;

import com.anhoang.socialnetworkdemo.entity.Post;
import com.anhoang.socialnetworkdemo.entity.PostReaction;
import com.anhoang.socialnetworkdemo.model.media.MediaDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    @JsonProperty("post_id")
    private Long id;
    @JsonProperty("is_shared")
    private Boolean isShared;
    @JsonProperty("post_role")
    private String postRole;
    @JsonProperty("post_avatar")
    private String postAvatar;
    @JsonProperty("post_type")
    private Post.PostType postType;
    @JsonProperty("post_name")
    private String postName;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("user_code")
    private String userCode;
    @JsonProperty("content")
    private String content;
    @JsonProperty("visibility")
    private Post.Visibility visibility;
    @JsonProperty("media_files")
    private List<MediaDto> mediaFiles;
    @JsonProperty("location")
    private String location;
    @JsonProperty("hash_tag")
    private List<String> hashTag;
    @JsonProperty("status")
    private Post.Status status;
    @JsonProperty("my_reaction")
    private PostReaction.Reaction myReaction;
    @JsonProperty("like_count")
    private Long likeCount;
    @JsonProperty("comment_count")
    private Long commentCount;
    @JsonProperty("shared_count")
    private Long sharedCount;
    @JsonProperty("shared_post")
    private SharedPostDto sharedPost;
    @JsonProperty("created_at")
    private String createdAt;
}
