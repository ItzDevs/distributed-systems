package dev.distributed.service.controllers;

import dev.distributed.service.database.BlogDb;
import dev.distributed.service.entities.Blog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j(topic = "BlogController")
@RequestMapping("/api/v1/articles")
public class BlogController {

    private final BlogDb blogDb;

    public BlogController(BlogDb blogDb) {
        this.blogDb = blogDb;
    }

    @GetMapping()
    public ResponseEntity<List<Blog>> getAllArticles() {
        List<Blog> articles = blogDb.findAll();

        if(articles.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(articles);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Blog>> getArticlesByAuthorId(@PathVariable UUID userId) {
        List<Blog> articles = blogDb.findAllByAuthor(userId);

        if(articles.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<Blog> getArticleById(@PathVariable UUID blogId) {
        Blog article = blogDb.findById(blogId).orElse(null);

        if(article == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(article);
    }
}
