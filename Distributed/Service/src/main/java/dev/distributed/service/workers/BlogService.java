package dev.distributed.service.workers;

import dev.distributed.contract.TimestampHelper;
import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.UpdateBlog;
import dev.distributed.service.database.BlogDb;
import dev.distributed.service.database.UserDb;
import dev.distributed.service.entities.Blog;
import dev.distributed.service.entities.User;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

// Regardless of the data source used, whether that be RMI or Kafka, the handling of blogs remains the same.
// This service abstracts the logic for handling the data so that RMI/Kafka only need to focus
// on consuming the data.

@Service
@NoArgsConstructor
@Slf4j(topic = "BlogService")
public class BlogService {
    private BlogDb blogDb;
    private UserDb userDb;

    // Spring Dependency Injection which is injecting both the JPA databases.
    @Autowired
    public BlogService(BlogDb blogDb,
                       UserDb userDb) {
        this.blogDb = blogDb;
        this.userDb = userDb;
    }

    public boolean postBlog(NewBlog blog) {
        try{
            log.info("Posting blog: {}", blog.getTitle());
            // Only post blogs by known users.
            User resolvedUser = userDb.findById(blog.getAuthorId()).orElse(null);
            if(resolvedUser == null){
                log.warn("Could not find user with id: {}", blog.getAuthorId());
                return false;
            }

            // Handle saving the blog to the database.
            Blog newBlog = Blog.toBlog(blog, resolvedUser);
            Blog saved = blogDb.save(newBlog);
            return saved != null;
        } catch(ConstraintViolationException e){
            log.error("Error posting blog: {}: Caused by {}", blog.getTitle(), e.getMessage());
            return false;
        } catch(Exception e){
            log.error("Error posting blog: {}", blog.getTitle(), e);
            return false;
        }
    }

    public boolean updateBlog(UpdateBlog blog) {
        log.info("Updating blog: {}", blog.getId());
        User resolvedUser = userDb.findById(blog.getAuthorId()).orElse(null);
        if (resolvedUser == null) {
            log.warn("Could not find user with id: {}", blog.getAuthorId());
            return false;
        }

        // Assert that the blog actually exists.
        Blog identifiedBlog = blogDb.findById(blog.getId()).orElse(null);
        if (identifiedBlog == null) {
            return false;
        }

        // Nothing to update - nothing further to validate.
        if (Blog.isSame(identifiedBlog, blog)) {
            log.info("Blog {} has no differences", blog.getId());
            return true;
        }
        // Assert that the user is the author of the blog.
        if(!identifiedBlog.getAuthor().getId().equals(resolvedUser.getId())){
            log.warn("User {} is not the author of blog {}", resolvedUser.getId(), blog.getId());
            return false;
        }

        // Update the blog with only the updated values
        if(blog.getTitle() != null && !identifiedBlog.getTitle().equals(blog.getTitle())){
            log.info("Updating blog title from {} to {}", identifiedBlog.getTitle(), blog.getTitle());
            identifiedBlog.setTitle(blog.getTitle());
        }
        if(blog.getContent() != null && !identifiedBlog.getContent().equals(blog.getContent())){
            log.info("Updating blog content from {} to {}", identifiedBlog.getContent(), blog.getContent());
            identifiedBlog.setContent(blog.getContent());
        }
        if(blog.getMediaUrls() != null && !Arrays.equals(identifiedBlog.getMediaUrls(), blog.getMediaUrls())){
            log.info("Updating blog mediaUrls from {} to {}",
                    String.join(", ", identifiedBlog.getMediaUrls()),
                    String.join(", ", blog.getMediaUrls()));
            identifiedBlog.setMediaUrls(blog.getMediaUrls());
        }
        if(blog.getTags() != null && !Arrays.equals(identifiedBlog.getTags(), blog.getTags())){
            log.info("Updating blog tags from {} to {}",
                    String.join(", ", identifiedBlog.getTags()),
                    String.join(", ", blog.getTags()));
            identifiedBlog.setTags(blog.getTags());
        }

        identifiedBlog.setUpdatedAt(TimestampHelper.getCurrentTimestamp());
        Blog updated = blogDb.save(identifiedBlog);

        return updated != null;
    }

    public boolean deleteBlog(RemoveBlog blog) {
        log.info("Deleting blog: {}", blog.getBlogId());
        User resolvedUser = userDb.findById(blog.getAuthorId()).orElse(null);
        if(resolvedUser == null){
            log.warn("Could not find user with id: {}", blog.getAuthorId());
            return false;
        }

        // If the blog does not exist, we don't remove anything so return true.
        Blog identifiedBlog = blogDb.findById(blog.getBlogId()).orElse(null);
        if(identifiedBlog == null){
            return true;
        }

        // Remove the blog
        blogDb.removeById(identifiedBlog.getId());
        return true;
    }
}
