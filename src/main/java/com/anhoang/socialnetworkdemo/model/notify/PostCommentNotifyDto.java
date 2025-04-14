package com.anhoang.socialnetworkdemo.model.notify;

import com.anhoang.socialnetworkdemo.entity.PostReaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentNotifyDto {
    @JsonProperty("comment_id")
    private Long commentId;
    @JsonProperty("comment_content")
    private String commentContent;
    @JsonProperty("reaction")
    private PostReaction.Reaction reaction;

    @JsonProperty("action_user_code")
    private String actionUserCode;
    @JsonProperty("action_username")
    private String actionUsername;
    @JsonProperty("action_user_avatar")
    private String actionUserAvatar;
}
