package com.anhoang.socialnetworkdemo.model.notify;

import com.anhoang.socialnetworkdemo.entity.PostReaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostNotifyDto {
    @JsonProperty("post_id")
    private Long postId;
    @JsonProperty("post_comment")
    private String postContent;
    @JsonProperty("my_reaction")
    private PostReaction.Reaction myReaction;
    @JsonProperty("my_comment")
    private String myComment;

    @JsonProperty("action_user_code")
    private String actionUserCode;
    @JsonProperty("action_username")
    private String actionUsername;
    @JsonProperty("action_user_avatar")
    private String actionUserAvatar;
}
