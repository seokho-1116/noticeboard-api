package com.example.noticeboardapi.post.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "POST_FILE")
@NoArgsConstructor
public class PostFile {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "file_id")
    private Long id;
    private String fileType;
    private String uploadFileName;
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
