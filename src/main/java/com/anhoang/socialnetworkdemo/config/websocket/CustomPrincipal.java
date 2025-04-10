package com.anhoang.socialnetworkdemo.config.websocket;

import java.security.Principal;

// CustomPrincipal class implements Principal interface
public class CustomPrincipal implements Principal {
    private final String name;

    public CustomPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
