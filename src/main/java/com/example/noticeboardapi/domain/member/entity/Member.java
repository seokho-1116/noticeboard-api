package com.example.noticeboardapi.domain.member.entity;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "MEMBER")
@Getter
public class Member {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(value = STRING)
    private Role role;

    private String name;

    private String nickname;

    private String email;

    @Column(name = "profile_file_id")
    private Long profileFileId;
}
