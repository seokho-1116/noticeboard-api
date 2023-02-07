package com.example.noticeboardapi.domain.member.entity;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "PROFILE_FILE")
public class ProfileFile {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "file_id")
    private Long id;
    private String storeFileName;
}