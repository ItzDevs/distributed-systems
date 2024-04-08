package dev.distributed.publisher.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.distributed.contract.dto.NewBlog;
import dev.distributed.contract.dto.RemoveBlog;
import dev.distributed.contract.dto.UpdateBlog;
import dev.distributed.contract.kafka.IEditorKafka;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "KafkaPublisher")
public class KafkaPublisher implements IEditorKafka {
    @Autowired
    public KafkaPublisher(ProducerFactory<String, String> producerFactory) {
        blogPublisher = new KafkaTemplate<>(producerFactory);
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    KafkaTemplate<String, String> blogPublisher;

    public void send(String topic, String data) {
        blogPublisher.send(topic, data);
    }

    @Override
    public boolean post(NewBlog blog) {
        try {
            String json = mapper.writeValueAsString(blog);
            log.info(json);
            send("newblog", json);
            return true;
        }
        catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean update(UpdateBlog blog) {
        try{
            String json = mapper.writeValueAsString(blog);
            log.info(json);
            send("updateblog", json);
            return true;
        }
        catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean delete(RemoveBlog blog) {
        try{
            String json = mapper.writeValueAsString(blog);
            log.info(json);
            send("deleteblog", json);
            return true;
        }
        catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
