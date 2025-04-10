package com.anhoang.socialnetworkdemo.model.media;

import com.anhoang.socialnetworkdemo.entity.MediaFile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaDto {
    @JsonProperty("file_size")
    private Long fileSize;
    @JsonProperty("file_url")
    private String fileUrl;
    @JsonProperty("format")
    private String format;
    @JsonProperty("value_type")
    private MediaFile.MediaType mediaType;
}
