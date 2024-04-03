package dev.distributed.service.database;

import dev.distributed.service.entities.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface BlogDb extends JpaRepository<Blog, UUID> {
//    @Query("SELECT b FROM Blog b WHERE :tag in b.tags")
//    List<Blog> findBlogsWithTag(@Param("tag") String tag);

    /**
     * Save or update a Blog
     *
     * @param blog The Record to save
     * @return The saved record
     */
    @Retryable(backoff = @Backoff(2000))
    @Modifying
    @Transactional
    <S extends Blog> S save(S blog);
}
