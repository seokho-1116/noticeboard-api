package com.example.noticeboardapi.domain.post.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "POST_FILE")
@NoArgsConstructor
public class PostFile {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "upload_file_name")
    private String uploadFileName;

    @Column(name = "store_file_name")
    private String storeFileName;

    public PostFile(String fileType, String uploadFileName, String storeFileName) {
        this.fileType = fileType;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }


    public static PostFile createPostFile(String fileType, String uploadFileName, String storeFileName) {
        return new PostFile(fileType, uploadFileName, storeFileName);
    }
}
