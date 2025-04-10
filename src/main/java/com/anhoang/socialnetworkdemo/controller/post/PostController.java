package com.anhoang.socialnetworkdemo.controller.post;

import com.anhoang.socialnetworkdemo.entity.Post;
import com.anhoang.socialnetworkdemo.entity.PostReaction;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.model.post.PostRequest;
import com.anhoang.socialnetworkdemo.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    private final ObjectMapper objectMapper;

    @GetMapping("/api/user/post/new")
    public ResponseEntity<?> userCreateHashtag(
            @RequestParam(name = "hashTag", required = false) String hashTag,
            @RequestParam("page_number") int pageNumber,
            @RequestParam("page_size") int pageSize
    ){
        return ResponseEntity.ok(postService.guestGetPostHome(hashTag, pageNumber, pageSize));
    }

    @PostMapping("/api/user/hashtag")
    public ResponseEntity<?> userCreateHashtag(
            @RequestParam("hashTag") String hashTag
    ){
        return ResponseEntity.ok(postService.userCreateHashTag(hashTag));
    }

    @PostMapping("/api/user/post/create")
    public ResponseEntity<?> userCreatePostApi(@RequestParam("post_json") String json,
                                               @RequestParam(name = "file", required = false) List<MultipartFile> listFile){
        try {
            PostRequest req = objectMapper.readValue(json, PostRequest.class);
            return ResponseEntity.ok(postService.userCreatePost(req, listFile));
        } catch (Exception e){
            throw new RequestNotFoundException("ERROR");
        }
    }

    @PutMapping("/api/user/post/update")
    public ResponseEntity<?> userUpdatePostApi(@RequestParam("post_json") String json,
                                               @RequestParam(name = "file", required = false) List<MultipartFile> listFile){
        try {
            PostRequest req = objectMapper.readValue(json, PostRequest.class);
            return ResponseEntity.ok(postService.userUpdatePost(req, listFile));
        } catch (Exception e){
            throw new RequestNotFoundException("ERROR");
        }
    }

    @DeleteMapping("/api/user/post/delete/{post_id}")
    public ResponseEntity<?> userCreatePostApi(@PathVariable("post_id") Long id){
        return ResponseEntity.ok(postService.userDeletePost(id));
    }

    @PostMapping("/api/user/post/shared")
    public ResponseEntity<?> userSharedPostApi(@RequestBody PostRequest req ){
        return ResponseEntity.ok(postService.userSharedPost(req));
    }

    @GetMapping("/api/user/post/my-post")
    public ResponseEntity<?> userGetTheirPostApi(@RequestParam(name = "hashtag", required = false) String postHashTag,
                                               @RequestParam("page_number") int pageNumber,
                                               @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(postService.userGetTheirListPost(postHashTag, pageNumber, pageSize));
    }

    @GetMapping("/api/user/post/user-post")
    public ResponseEntity<?> userSharedPostApi(@RequestParam("user_code") String userCode,
                                               @RequestParam(name = "hashtag", required = false) String hashTag,
                                               @RequestParam("page_number") int pageNumber,
                                               @RequestParam("page_size") int pageSize){
        return ResponseEntity.ok(postService.userGetUserPostOther(userCode, hashTag, pageNumber, pageSize));
    }

    @PutMapping("/api/admin/post/active")
    public ResponseEntity<?> adminActivePost(@RequestParam("post_id") Long postId,
                                             @RequestParam("status") Post.Status status){
        return ResponseEntity.ok(postService.adminActivePost(postId, status));
    }

    @PostMapping("/api/user/post/reaction")
    public ResponseEntity<?> userReactionPost(@RequestParam("post_id") Long postId,
                                              @RequestParam("reaction") PostReaction.Reaction reaction){
        return ResponseEntity.ok(postService.useReactionPost(postId, reaction));
    }

    @GetMapping("/api/user/post/post-detail/{post_id}")
    public ResponseEntity<?> userGetPostDetail(@PathVariable("post_id") Long postId){
        return ResponseEntity.ok(postService.userGetPostDetail(postId));
    }
}
