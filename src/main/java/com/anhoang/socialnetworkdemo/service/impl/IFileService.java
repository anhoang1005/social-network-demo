package com.anhoang.socialnetworkdemo.service.impl;

import com.anhoang.socialnetworkdemo.exceptions.external.FileFailedUploadException;
import com.anhoang.socialnetworkdemo.exceptions.request.RequestNotFoundException;
import com.anhoang.socialnetworkdemo.model.media.MessageFileDto;
import com.anhoang.socialnetworkdemo.payload.ResponseBody;
import com.anhoang.socialnetworkdemo.service.FileService;
import com.anhoang.socialnetworkdemo.utils.FilesUtils;
import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Slf4j
public class IFileService implements FileService {
    private final Cloudinary cloudinary;
    private final Path fileStorageLocation;
    private final String serverDomain;
    private String uploadFilePath = "uploads/message/";

    @Autowired
    public IFileService(Cloudinary cloudinary,
                        @Value("${server.domain}") String serverDomain) {
        this.serverDomain = serverDomain;
        this.cloudinary = cloudinary;
        this.fileStorageLocation = Paths.get("uploads/message/").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Không thể tạo thư mục lưu file", ex);
        }
    }

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

    @Override
    public ResponseBody<?> uploadToServer(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        //Ten file duy nhat
        String uniqueFileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;
        try {
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            MessageFileDto fileDto = FilesUtils.convertFileToMetadata(file);
            fileDto.setFileNameSave(uniqueFileName);
            fileDto.setFileUrl(serverDomain + "/api/guest/files/view/" + uniqueFileName);
            fileDto.setDownloadUrl(serverDomain + "/api/guest/files/download/" + uniqueFileName);
            return new ResponseBody<>(fileDto, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (IOException ex) {
            log.error("Upload file error! " + ex.getMessage());
            throw new RequestNotFoundException("Upload error! Error: " + ex.getMessage());
        }
    }


    @Override
    public ResponseBody<?> uploadMultiFileToServer(MultipartFile[] files) {
        if (files.length == 0) {
            throw new RequestNotFoundException("Upload error!");
        }
        List<MessageFileDto> uploadedFiles = Arrays.stream(files).parallel()
                .map(file -> {
                    String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                    String extension = "";
                    int dotIndex = originalFilename.lastIndexOf(".");
                    if (dotIndex > 0) {
                        extension = originalFilename.substring(dotIndex);
                        originalFilename = originalFilename.substring(0, dotIndex);
                    }
                    //Tao ten file duy nhat
                    String uniqueFileName = originalFilename + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;
                    try {
                        Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
                        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                        MessageFileDto fileDto = FilesUtils.convertFileToMetadata(file);
                        fileDto.setFileNameSave(uniqueFileName);
                        fileDto.setFileUrl(serverDomain + "/api/guest/files/view/" + uniqueFileName);
                        fileDto.setDownloadUrl(serverDomain + "/api/guest/files/download/" + uniqueFileName);
                        return fileDto;
                    } catch (IOException ex) {
                        return null;
                    }
                })
                .filter(Objects::nonNull).toList();
        if (uploadedFiles.isEmpty()) {
            throw new RequestNotFoundException("Không thể upload file nào.");
        }
        return new ResponseBody<>(uploadedFiles, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
    }

    @Override
    public ResponseBody<?> deleteFileInServer(Long fileId) {
        return null;
    }
}
