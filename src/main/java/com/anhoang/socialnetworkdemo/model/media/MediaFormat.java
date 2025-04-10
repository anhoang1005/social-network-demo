package com.anhoang.socialnetworkdemo.model.media;

import com.anhoang.socialnetworkdemo.entity.MediaFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaFormat {
    private String format;
    private MediaFile.MediaType mediaType;
}
