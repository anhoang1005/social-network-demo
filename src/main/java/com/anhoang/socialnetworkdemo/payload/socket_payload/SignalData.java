package com.anhoang.socialnetworkdemo.payload.socket_payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignalData {

    private Type type;
    private String conversationId;
    private Object data;

    public enum Type{
        OFFER,
        ANSWER,
        CANDIDATE
    }
}
