package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.entity.PostReaction;
import com.anhoang.socialnetworkdemo.model.post.post_comment.PostCommentRequest;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface PostCommentService {

    ResponseBody<?> userGetListComment(Long postId, Long commentId, int pageNumber);
    ResponseBody<?> userCreateComment(PostCommentRequest dto, MultipartFile file);
    ResponseBody<?> userUpdateComment(PostCommentRequest dto, MultipartFile file);
    ResponseBody<?> userDeleteComment(Long commentId);
    ResponseBody<?> userReactionComment(Long commentId, PostReaction.Reaction reaction);
}
