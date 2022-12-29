package com.example.noticeboardapi.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class UploadFile {
    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;
    private String fileType;
    private String uploadFileName;
    private String storeFileName;
}
