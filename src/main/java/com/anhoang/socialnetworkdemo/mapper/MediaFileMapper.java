package com.anhoang.socialnetworkdemo.mapper;

import com.anhoang.socialnetworkdemo.entity.MediaFile;
import com.anhoang.socialnetworkdemo.model.media.MediaFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public class MediaFileMapper implements Serializable {

    public static MediaFormat getFormatOfFile(MultipartFile file){
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        contentType = contentType.toLowerCase();
        MediaFormat mediaFormat = new MediaFormat();
        if (contentType == null || filename == null || !filename.contains(".")) {
            return mediaFormat;
        }
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        mediaFormat.setFormat(extension);

        if (contentType.startsWith("image/") || isImageExtension(extension)) {
            mediaFormat.setMediaType(MediaFile.MediaType.IMAGE);
        } else if (contentType.startsWith("video/") || isVideoExtension(extension)) {
            mediaFormat.setMediaType(MediaFile.MediaType.VIDEO);
        } else if (contentType.startsWith("audio/") || isAudioExtension(extension)) {
            mediaFormat.setMediaType(MediaFile.MediaType.AUDIO);
        } else if (isDocumentExtension(extension)) {
            mediaFormat.setMediaType(MediaFile.MediaType.DOCUMENT);
        } else {
            mediaFormat.setMediaType(MediaFile.MediaType.OTHER);
        }
        return mediaFormat;
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
