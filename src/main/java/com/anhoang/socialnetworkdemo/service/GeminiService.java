package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.model.gemini.GeminiDto;
import com.anhoang.socialnetworkdemo.model.post.PostRequest;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface GeminiService {

    ResponseBody<?> chatWithGeminiText(String message);

    ResponseBody<?> chatWithGeminiImage(String message, MultipartFile file);

//    ResponseBody<?> chatWithGeminiTextWithPrompt(GeminiDto dto);
//
    ResponseBody<?> chatWithGeminiTextWithPromptText(String promptText, String message);


    ResponseBody<?> aiCheckPostCreate(PostRequest req);
}
