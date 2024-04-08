package dev.distributed.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.UpdateBlog;
import dev.distributed.contract.kafka.IEditorKafka;
import dev.distributed.service.workers.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "KafkaEditorService")
public class KafkaEditorService implements IEditorKafka {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final BlogService blogService;

    @Autowired
    public KafkaEditorService(BlogService blogService) {
        this.blogService = blogService;
    }

    @KafkaListener(topics = { "newblog" }, groupId = "distributed-consumer")
    public void postBlog(String message) {
        try{
            log.info("Received a new blog from Kafka  " + message);
            NewBlog blog = objectMapper.readValue(message, NewBlog.class);

            boolean result = blogService.postBlog(blog);
            if(result){
                log.info("Posted blog: {}", blog.getTitle());
            } else{
                log.warn("Failed to post blog: {}", blog.getTitle());
            }
        }
        catch(Exception e){
            log.error(e.getMessage(), e);
        }
    }

    @KafkaListener(topics = { "updateblog"}, groupId = "distributed-consumer")
    public void updateBlog(String message){
        try{
            log.info("Received an updated blog from Kafka  " + message);
            UpdateBlog blog = objectMapper.readValue(message, UpdateBlog.class);

            boolean result = blogService.updateBlog(blog);
            if(result){
                log.info("Updated blog: {}", blog.getId());
            } else{
                log.warn("Failed to update blog: {}", blog.getId());
            }
        }
        catch(Exception e){
            log.error(e.getMessage(), e);
        }
    }

    @KafkaListener(topics = { "deleteblog" }, groupId = "distributed-consumer")
    public void deleteBlog(String message){
        try{
            log.info("Received a new blog from Kafka  " + message);
            RemoveBlog blog = objectMapper.readValue(message, RemoveBlog.class);

            boolean result = blogService.deleteBlog(blog);
            if(result){
                log.info("Deleted blog: {}", blog.getBlogId());
            }
            else{
                log.warn("Failed to delete blog: {}", blog.getBlogId());
            }
        }
        catch(Exception e){
            log.error(e.getMessage(), e);
        }
    }
}
