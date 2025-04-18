package com.anhoang.socialnetworkdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoryViewer {

    private Story story;
    private Reaction reaction;

    public enum Reaction{
        LIKE,
        HAHA,
        LOVE,
        ANGRY,
        SAD,
        WOW
    }
}
