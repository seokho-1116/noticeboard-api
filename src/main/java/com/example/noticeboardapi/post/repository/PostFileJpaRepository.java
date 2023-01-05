package com.example.noticeboardapi.post.repository;

import com.example.noticeboardapi.post.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileJpaRepository extends JpaRepository<PostFile, Long> {
}
