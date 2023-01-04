package com.example.noticeboardapi.member.entity;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "MEMBER")
public class Member {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(value = STRING)
    private Role role;
    private String name;
    private String nickname;
    private String email;
    private Long profileFileId;
}
