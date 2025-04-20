package com.anhoang.socialnetworkdemo.model.story;

import com.anhoang.socialnetworkdemo.entity.Story;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryRequest {
    private String mediaUrl;
    private String capture;
    private String backgroundColor;
    private String music;
    private Story.Type type;
    private Story.Visibility visibility;
}
