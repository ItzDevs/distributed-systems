package dev.distributed.service.workers;


import dev.distributed.service.database.BlogDb;
import dev.distributed.service.entities.Blog;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@NoArgsConstructor
public class BlogService {
    private BlogDb blogDb;

    @Autowired
    public BlogService(BlogDb blogDb) {
        this.blogDb = blogDb;
    }

    public Blog resolveBlog(UUID blogId) {
        return this.blogDb.findById(blogId).orElse(null);
    }

}
