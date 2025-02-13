package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.exceptions.external.FileFailedUploadException;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.service.FileService;
import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@Service
@Slf4j
public class IFileService implements FileService {
    private final Cloudinary cloudinary;

    //upload file len cloud
    @Override
    public ResponseBody<?> uploadToCloudinary(MultipartFile file)  {
        try{
            //Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            var data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            //System.out.println(data.get("url"));
            return new ResponseBody<>(data.get("url"), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (IOException io){
            log.info(io.getMessage());
            throw new FileFailedUploadException("Failed upload file");
        }
    }
}
