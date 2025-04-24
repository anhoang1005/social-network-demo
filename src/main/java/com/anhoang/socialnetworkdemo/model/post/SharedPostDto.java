package com.anhoang.socialnetworkdemo.model.post;

import com.anhoang.socialnetworkdemo.entity.MediaFile;
import com.anhoang.socialnetworkdemo.entity.Post;
import com.anhoang.socialnetworkdemo.model.media.MediaDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedPostDto {
    @JsonProperty("shared_post_id")
    private Long sharedPostId;
    @JsonProperty("post_role")
    private String postRole;
    @JsonProperty("shared_post_avatar")
    private String sharedPostAvatar;
    @JsonProperty("shared_post_name")
    private String sharedPostName;
    @JsonProperty("shared_post_type")
    private Post.PostType sharedPostType;
    @JsonProperty("content")
    private String content;
    @JsonProperty("media_files")
    private List<MediaDto> mediaFiles;
    @JsonProperty("hash_tag")
    private List<String> hashTag;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("like_count")
    private Long likeCount;
    @JsonProperty("comment_count")
    private Long commentCount;
    @JsonProperty("shared_count")
    private Long sharedCount;
}
