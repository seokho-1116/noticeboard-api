package com.example.noticeboardapi.entity;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class PostFile {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "file_id")
    private Long id;
    private String fileType;
    private String uploadFileName;
    private String storeFileName;
}
