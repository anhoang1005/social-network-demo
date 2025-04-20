package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.entity.StoryViewer;
import com.anhoang.socialnetworkdemo.model.story.StoryRequest;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface StoryService {

    ResponseBody<?> getNewFeedStory();

    ResponseBody<?> userCreateStory(StoryRequest req, MultipartFile file);

    ResponseBody<?> updateStory(StoryRequest req, MultipartFile file);

    ResponseBody<?> deleteStory(Long storyId);

    ResponseBody<?> viewAndReactionStory(Long storyId, StoryViewer.Reaction reaction);
}
