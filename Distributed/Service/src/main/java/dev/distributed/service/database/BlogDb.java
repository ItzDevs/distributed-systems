package dev.distributed.service.database;

import dev.distributed.service.entities.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

// JPA repository for the Blog.
@Repository
@Transactional
public interface BlogDb extends JpaRepository<Blog, UUID> {

    /**
     * Remove a Blog
     *
     * @param id The id of the Blog to remove
     * @return The removed Blog
     */
    <S extends Blog> S removeById(UUID id);

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

    @Query("SELECT b FROM Blog b WHERE b.author.id = :author")
    List<Blog> findAllByAuthor(UUID author);

}
