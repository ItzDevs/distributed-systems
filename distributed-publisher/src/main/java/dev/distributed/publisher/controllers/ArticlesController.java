package dev.distributed.publisher.controllers;

import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.UpdateBlog;
import dev.distributed.publisher.rmi.RmiPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;
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
}
