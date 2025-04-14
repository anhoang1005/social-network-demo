package com.anhoang.socialnetworkdemo.payload.socket_payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocketBody<T> {
    private T body;
    private Type type;
    private String createdAt;

    public enum Type{
        MESSAGE,
        NOTIFY
    }
}
