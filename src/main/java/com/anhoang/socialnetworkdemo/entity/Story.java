package com.anhoang.socialnetworkdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Story {

    private String mediaUrl;
    private String capture;
    private String backgroundColor;
    private String music;
    private Type type;

    public enum Type{
        IMAGE,
        VIDEO,
        TEXT
    }
}
