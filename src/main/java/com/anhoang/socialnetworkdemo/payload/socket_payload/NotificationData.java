package com.anhoang.socialnetworkdemo.payload.socket_payload;

import com.anhoang.socialnetworkdemo.entity.Notifications;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationData {

    @JsonProperty("notify_id")
    private Long notifyId;
    @JsonProperty("type")
    private Notifications.Type type;
    @JsonProperty("content")
    private String content;
    @JsonProperty("isRead")
    private Boolean isRead;
    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("target_data")
    private Object targetData;
    @JsonProperty("other_url")
    private String otherUrl;
}
