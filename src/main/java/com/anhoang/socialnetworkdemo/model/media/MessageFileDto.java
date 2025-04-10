package com.anhoang.socialnetworkdemo.model.media;

import com.anhoang.socialnetworkdemo.entity.MessageFile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageFileDto {
    @JsonProperty("file_id")
    private Long fileId;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("file_name_save")
    private String fileNameSave;
    @JsonProperty("file_size")
    private Long fileSize;
    @JsonProperty("file_type")
    private MessageFile.MediaType fileType;
    @JsonProperty("file_format")
    private String fileFormat;
    @JsonProperty("file_url")
    private String fileUrl;
    @JsonProperty("download_url")
    private String downloadUrl;
}
