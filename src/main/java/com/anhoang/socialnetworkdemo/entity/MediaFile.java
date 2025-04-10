package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media_file")
public class MediaFile extends BaseEntity<Long> {
    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String fileName;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType mediaType; // Kiểu phương tiện (HÌNH ẢNH, VIDEO, AUDIO, FILE)
    private String mediaFormat; // Định dạng nén (JPEG, WEBP, MP4_H265,...)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel; // Quyền riêng tư (PUBLIC, FRIENDS_ONLY, PRIVATE)
    private String albumName; // Tên album (nếu có)

    @ManyToOne
    @JoinColumn(name = "uploaded_by", nullable = false)
    private Users usersUpdated; // Người tải lên file

    private boolean isPublic; // Công khai hay không
    private boolean isDeleted; // Đánh dấu nếu file bị xoá

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private PostComment postComment; // Liên kết đến bình luận nếu file thuộc về bình luận

    public enum MediaType {
        IMAGE, VIDEO, AUDIO, DOCUMENT, OTHER
    }

    public enum AccessLevel {
        PUBLIC, FRIENDS_ONLY, PRIVATE
    }

    @PrePersist
    void create() {
        this.isDeleted = false;
    }
}