package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message_file")
public class MessageFile extends BaseEntity<Long> implements Serializable {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String fileName;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String fileNameSave;
    @Column(nullable = false)
    private Long fileSize;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType mediaType; // Kiểu phương tiện (HÌNH ẢNH, VIDEO, AUDIO, FILE)
    @Column(nullable = false)
    private String mediaFormat; // Định dạng nén (JPEG, WEBP, MP4_H265,...)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uploaded_by", referencedColumnName = "id")
    private Users usersUpdated;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    public enum MediaType {
        IMAGE, VIDEO, AUDIO, DOCUMENT, OTHER
    }
}
