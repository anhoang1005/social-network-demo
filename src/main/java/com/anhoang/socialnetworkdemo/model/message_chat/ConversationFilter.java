package com.anhoang.socialnetworkdemo.model.message_chat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationFilter {
    private String conversationName;
    private String userCode;
    private Integer pageNumber;
    private Integer pageSize;
}
