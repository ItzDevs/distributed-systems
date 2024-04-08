package dev.distributed.publisher.controllers;

import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.UpdateBlog;
import dev.distributed.publisher.rmi.RmiPublisher;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;
import java.util.UUID;

@RestController
@Slf4j(topic = "RmiArticlesController")
@RequestMapping("/api/v1/rmi/articles")
public class RmiArticlesController {

    private final RmiPublisher rmiPublisher;

    @Autowired
    public RmiArticlesController(RmiPublisher rmiPublisher) {
        this.rmiPublisher = rmiPublisher;
    }


    @GetMapping("/addtemp")
    public String hello() {
        try{
            rmiPublisher.post(new NewBlog("testing", "this is a test", null, new String[] { "t1", "t2" }, UUID.fromString("0fc17ba5-f56a-486b-9939-916da84ae676")));
            return "Hello World!";
        } catch(Exception e){
            log.error(e.getMessage(), e);
            return "Oops";
        }
    }

    @GetMapping("/updatetemp")
    public String hello2() {
        try{
            rmiPublisher.update(new UpdateBlog(UUID.fromString("7efc9a1e-e3e1-4ae3-bb05-5f3e92c41383"), null, "this is an update test", null, new String[] { "t1", "t2" }, UUID.fromString("0fc17ba5-f56a-486b-9939-916da84ae676")));
            return "Hello World!";
        } catch(RemoteException e){
            log.error(e.getMessage(), e);
            return "Oops";
        }
    }

    @GetMapping("/deletetemp")
    public String hello3() {
        try{
            rmiPublisher.delete(new RemoveBlog(UUID.fromString("7efc9a1e-e3e1-4ae3-bb05-5f3e92c41383"), UUID.fromString("0fc17ba5-f56a-486b-9939-916da84ae676")));
            return "Hello World!";
        } catch(RemoteException e){
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
            boolean posted = rmiPublisher.post(newBlog);
            if(posted)
                return new ResponseEntity<>("Article created", HttpStatus.CREATED);
            else
                return new ResponseEntity<>("Failed to save article", HttpStatus.BAD_REQUEST);
        } catch (RemoteException e) {
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
            boolean posted = rmiPublisher.update(updateBlog);
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
            boolean posted = rmiPublisher.delete(removeBlog);
            if(posted)
                return new ResponseEntity<>("Article deleted", HttpStatus.ACCEPTED);
            else
                return new ResponseEntity<>("Failed to delete article", HttpStatus.BAD_REQUEST);
        } catch(Exception e){
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
