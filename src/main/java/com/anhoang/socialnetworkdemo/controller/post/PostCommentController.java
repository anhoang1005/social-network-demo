package com.anhoang.socialnetworkdemo.controller.post;

import com.anhoang.socialnetworkdemo.entity.PostReaction;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.model.post.post_comment.PostCommentRequest;
import com.anhoang.socialnetworkdemo.service.PostCommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class PostCommentController {
    private final PostCommentService pcmService;
    private final ObjectMapper objectMapper;

    @GetMapping("/api/user/post/get-comment")
    public ResponseEntity<?> getListCommentOfPost(@RequestParam(name = "post_id", required = false) Long postId,
                                                  @RequestParam(name = "comment_id", required = false) Long commentId,
                                                  @RequestParam("page_number") int pageNumber){
        return ResponseEntity.ok(pcmService.userGetListComment(postId, commentId, pageNumber));
    }

    @PostMapping("/api/user/post/create-comment")
    public ResponseEntity<?> userCreateCommentOfPost(@RequestParam("comment_json") String json,
                                                     @RequestParam(name = "file", required = false) MultipartFile file){
        try {
            PostCommentRequest req = objectMapper.readValue(json, PostCommentRequest.class);
            return ResponseEntity.ok(pcmService.userCreateComment(req, file));
        } catch (Exception e){
            e.printStackTrace();
            throw new RequestNotFoundException("ERROR");
        }
    }

    @PutMapping("/api/user/post/update-comment")
    public ResponseEntity<?> userUpdateCommentOfPost(@RequestParam("comment_json") String json,
                                                     @RequestParam(name = "file", required = false) MultipartFile file){
        try {
            PostCommentRequest req = objectMapper.readValue(json, PostCommentRequest.class);
            return ResponseEntity.ok(pcmService.userUpdateComment(req, file));
        } catch (Exception e){
            throw new RequestNotFoundException("ERROR");
        }
    }

    @DeleteMapping("/api/user/post/delete-comment/{comment_id}")
    public ResponseEntity<?> getListCommentOfPost(@PathVariable(name = "comment_id") Long commentId){
        return ResponseEntity.ok(pcmService.userDeleteComment(commentId));
    }

    @PostMapping("/api/user/post/reaction-comment/{comment_id}")
    public ResponseEntity<?> userReactionComment(@PathVariable(name = "comment_id") Long commentId,
                                                 @RequestParam("reaction") PostReaction.Reaction reaction){
        return ResponseEntity.ok(pcmService.userReactionComment(commentId, reaction));
    }
}
