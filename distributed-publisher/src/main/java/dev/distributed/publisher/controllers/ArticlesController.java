package dev.distributed.publisher.controllers;

import dev.distributed.contract.dto.NewBlog;
import dev.distributed.publisher.rmi.RmiPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j(topic = "ArticlesController")
@RequestMapping("/api/v1/articles")
public class ArticlesController {

    private final RmiPublisher rmiPublisher;

    @Autowired
    public ArticlesController(RmiPublisher rmiPublisher) {
        this.rmiPublisher = rmiPublisher;
    }


    @GetMapping("/hello")
    public String hello() {
        try{
            rmiPublisher.post(new NewBlog("testing", "this is a test", null, new String[] { "t1", "t2" }, UUID.fromString("60319f5d-0511-4faa-8c1f-fd8cc0a5f297")));
            return "Hello World!";
        }
        catch(Exception e){
            log.error(e.getMessage(), e);
            return "Oops";
        }

    }
}
