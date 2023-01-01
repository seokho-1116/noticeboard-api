package com.example.noticeboardapi.post.repository;

import com.example.noticeboardapi.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
}
