package com.anhoang.socialnetworkdemo.service;

import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    ResponseBody<?> uploadToCloudinary(MultipartFile file);
}
