package com.anhoang.socialnetworkdemo.utils;

import com.anhoang.socialnetworkdemo.entity.MessageFile;
import com.anhoang.socialnetworkdemo.model.media.MessageFileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FilesUtils {

    public static MessageFileDto convertFileToMetadata(MultipartFile file){
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        contentType = contentType.toLowerCase();
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        MessageFile.MediaType mediaType = null;
        if (contentType.startsWith("image/") || isImageExtension(extension)) {
            mediaType = MessageFile.MediaType.IMAGE;
        } else if (contentType.startsWith("video/") || isVideoExtension(extension)) {
            mediaType = MessageFile.MediaType.VIDEO;
        } else if (contentType.startsWith("audio/") || isAudioExtension(extension)) {
            mediaType = MessageFile.MediaType.AUDIO;
        } else if (isDocumentExtension(extension)) {
            mediaType = MessageFile.MediaType.DOCUMENT;
        } else {
            mediaType = MessageFile.MediaType.OTHER;
        }
        return MessageFileDto.builder()
                .fileName(filename)
                .fileNameSave(null)
                .fileSize(file.getSize())
                .fileType(mediaType)
                .fileFormat(extension)
                .fileUrl(null)
                .build();
    }

    private static boolean isImageExtension(String ext) {
        return List.of("jpg", "jpeg", "png", "gif", "bmp", "webp").contains(ext);
    }

    private static boolean isVideoExtension(String ext) {
        return List.of("mp4", "avi", "mov", "wmv", "flv", "mkv", "webm").contains(ext);
    }

    private static boolean isDocumentExtension(String ext) {
        return List.of("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "csv").contains(ext);
    }

    private static boolean isAudioExtension(String ext) {
        return List.of("mp3", "wav", "ogg", "aac", "flac", "m4a", "wma").contains(ext);
    }
}
