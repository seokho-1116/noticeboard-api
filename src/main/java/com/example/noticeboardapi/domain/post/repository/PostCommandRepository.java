package com.example.noticeboardapi.domain.post.repository;

import com.example.noticeboardapi.domain.post.entity.Post;
import com.example.noticeboardapi.domain.post.entity.PostFile;
import com.example.noticeboardapi.domain.post.exception.NoSuchPostException;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.test.tables.records.PostFileRecord;
import org.jooq.generated.test.tables.records.PostRecord;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.generated.test.Tables.*;
import static org.jooq.generated.test.tables.Post.POST;
import static org.jooq.generated.test.tables.PostFile.POST_FILE;

@Repository
@RequiredArgsConstructor
public class PostCommandRepository {

    private final DSLContext dslContext;

    public void updateViewCount(Long postNo) {
        PostRecord postRecord = getPostRecord(postNo);

        postRecord.setViewCount(postRecord.getViewCount() + 1);
        postRecord.store(POST.VIEW_COUNT);
    }

    public void updateRecommendationCount(Long postNo) {
        PostRecord postRecord = getPostRecord(postNo);

        postRecord.setViewCount(postRecord.getRecommendationCount() + 1);
        postRecord.store(POST.RECOMMENDATION_COUNT);
    }

    private PostRecord getPostRecord(Long postNo) {
        PostRecord postRecord = dslContext.fetchOne(POST, POST.POST_ID.eq(postNo));

        if (postRecord == null) {
            throw new NoSuchPostException();
        }

        return postRecord;
    }

    public void deletePost(Long postNo) {
        PostRecord postRecord = getPostRecord(postNo);

        postRecord.delete();
    }

    public void updatePost(Long postNo, String updateTitle, String updateText, List<PostFile> postFiles) {
        PostRecord postRecord = dslContext.fetchOne(POST, POST.POST_ID.eq(postNo));
        LocalDateTime updateTime = LocalDateTime.now();

        postRecord.setLastModifiedTime(updateTime);
        postRecord.setTitle(updateTitle);
        postRecord.setText(updateText);
        postRecord.store(POST.LAST_MODIFIED_TIME, POST.TITLE, POST.TEXT);

        if (postFiles != null) {
            dslContext.delete(POST_FILE)
                    .where(POST_FILE.POST_ID.eq(postNo))
                    .execute();

            List<PostFileRecord> postFileRecords = new ArrayList<>();
            for (PostFile postFile : postFiles) {
                PostFileRecord postFileRecord = dslContext.newRecord(POST_FILE, postFile);
                postFileRecord.setPostId(postNo);
                postFileRecords.add(postFileRecord);
            }
            dslContext.batchInsert(postFileRecords).execute();
        }
    }
}
