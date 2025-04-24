package com.anhoang.socialnetworkdemo.controller.users;

import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.model.post.PostRequest;
import com.anhoang.socialnetworkdemo.service.PostService;
import com.anhoang.socialnetworkdemo.service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class MediaController {
    private final PostService postService;
    private final ObjectMapper objectMapper;

    @PutMapping("/api/user/avatar/change")
    public ResponseEntity<?> userChangeImageUrlApi(
            @RequestParam("post_json") String json,
            @RequestParam("file") MultipartFile file){
        try {
            PostRequest req = objectMapper.readValue(json, PostRequest.class);
            return ResponseEntity.ok(postService.userCreateChangeAvatarPost(req, file));
        } catch (Exception e){
            throw new RequestNotFoundException("ERROR");
        }
    }

    @PutMapping("/api/user/cover-image/change")
    public ResponseEntity<?> userChangeCoverImageApi(
            @RequestParam("post_json") String json,
            @RequestParam("file") MultipartFile file){
        try {
            PostRequest req = objectMapper.readValue(json, PostRequest.class);
            return ResponseEntity.ok(postService.userCreateChangeCoverImagePost(req, file));
        } catch (Exception e){
            throw new RequestNotFoundException("ERROR");
        }
    }
}
