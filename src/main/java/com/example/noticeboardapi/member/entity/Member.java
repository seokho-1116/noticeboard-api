package com.example.noticeboardapi.member.entity;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
public class Member {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private Role role;
    private String name;
    private String nickname;
    private String email;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profileFile_id")
    private ProfileFile profileFile;
}
