package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.entity.Hashtag;
import com.anhoang.socialnetworkdemo.entity.Post;
import com.anhoang.socialnetworkdemo.entity.PostReaction;
import com.anhoang.socialnetworkdemo.model.post.PostRequest;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    ResponseBody<?> guestGetPostHome(String hashtag, int pageNumber, int pageSize);
    Hashtag userCreateHashTag(String hashtag);
    ResponseBody<?> userCreatePost(PostRequest req, List<MultipartFile> listFile);
    ResponseBody<?> userUpdatePost(PostRequest req, List<MultipartFile> listFile);
    ResponseBody<?> userDeletePost(Long postId);
    ResponseBody<?> userSharedPost(PostRequest req);
    ResponseBody<?> userGetTheirListPost(String postHashtag, int pageNumber, int pageSize);
    ResponseBody<?> userGetUserPostOther(String userCode, String hashTag, int pageNumber, int pageSize);
    ResponseBody<?> adminActivePost(Long postId, Post.Status status);
    ResponseBody<?> useReactionPost(Long postId, PostReaction.Reaction reaction);
}
