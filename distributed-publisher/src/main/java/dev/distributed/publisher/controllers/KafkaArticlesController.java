package dev.distributed.publisher.controllers;

import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.UpdateBlog;
import dev.distributed.publisher.kafka.KafkaPublisher;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@Slf4j(topic = "KafkaArticlesController")
@RequestMapping("/api/v1/kafka/articles")
public class KafkaArticlesController {

    private final KafkaPublisher publisher;

    @Autowired
    public KafkaArticlesController(KafkaPublisher publisher) {
        this.publisher = publisher;
    }



    @GetMapping("/addtemp")
    public String hello() {
        try{
            publisher.post(new NewBlog("testing", "this is a test", null, new String[] { "t1", "t2" }, UUID.fromString("0fc17ba5-f56a-486b-9939-916da84ae676")));
            return "Hello World!";
        } catch(Exception e){
            log.error(e.getMessage(), e);
            return "Oops";
        }
    }

    @GetMapping("/updatetemp")
    public String hello2() {
        try{
            publisher.update(new UpdateBlog(UUID.fromString("0d6a3e0e-c1e9-4cbe-8189-c9b34ccafc94"), null, "this is an update test", null, new String[] { "t1", "t2" }, UUID.fromString("0fc17ba5-f56a-486b-9939-916da84ae676")));
            return "Hello World!";
        } catch(Exception e){
            log.error(e.getMessage(), e);
            return "Oops";
        }
    }

    @GetMapping("/deletetemp")
    public String hello3() {
        try{
            publisher.delete(new RemoveBlog(UUID.fromString("7efc9a1e-e3e1-4ae3-bb05-5f3e92c41383"), UUID.fromString("0fc17ba5-f56a-486b-9939-916da84ae676")));
            return "Hello World!";
        } catch(Exception e){
            log.error(e.getMessage(), e);
            return "Oops";
        }
    }


    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Article created."),
            @ApiResponse(responseCode = "400", description = "Failed to save article."),
            @ApiResponse(responseCode = "500", description = "Something went wrong.")}
    )
    public ResponseEntity<String> createArticle(NewBlog newBlog) {
        try {
            boolean posted = publisher.post(newBlog);
            if(posted)
                return new ResponseEntity<>("Article created", HttpStatus.CREATED);
            else
                return new ResponseEntity<>("Failed to save article", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{articleUuid}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Article updated."),
            @ApiResponse(responseCode = "400", description = "Failed to update article."),
            @ApiResponse(responseCode = "500", description = "Something went wrong.")}
    )
    public ResponseEntity<String> updateArticle(@PathVariable UUID articleUuid, UpdateBlog updateBlog) {
        updateBlog.setId(articleUuid);

        try{
            boolean posted = publisher.update(updateBlog);
            if(posted)
                return new ResponseEntity<>("Article updated", HttpStatus.ACCEPTED);
            else
                return new ResponseEntity<>("Failed to save article", HttpStatus.BAD_REQUEST);
        } catch(Exception e){
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{articleUuid}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Article updated."),
            @ApiResponse(responseCode = "400", description = "Failed to delete article."),
            @ApiResponse(responseCode = "500", description = "Something went wrong.")}
    )
    public ResponseEntity<String> deleteArticle(@PathVariable UUID articleUuid, RemoveBlog removeBlog) {
        removeBlog.setBlogId(articleUuid);

        try{
            boolean posted = publisher.delete(removeBlog);
            if(posted)
                return new ResponseEntity<>("Article deleted", HttpStatus.ACCEPTED);
            else
                return new ResponseEntity<>("Failed to delete article", HttpStatus.BAD_REQUEST);
        } catch(Exception e){
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
