package com.anhoang.socialnetworkdemo.payload.socket_payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationData {
    private Long notifyId;
    private String senderCode;
    private String senderName;
    private String image;
    private String title;
    private String detail;
}
