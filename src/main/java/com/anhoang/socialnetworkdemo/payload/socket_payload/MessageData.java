package com.anhoang.socialnetworkdemo.payload.socket_payload;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageData {
    private Long conversationId;
    private String userCode;
    private Long replyOf;
    private String content;
    private String mediaUrl;
}
