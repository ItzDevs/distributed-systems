package dev.distributed.service.controllers;

import dev.distributed.service.database.UserDb;
import dev.distributed.service.entities.Blog;
import dev.distributed.service.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@Slf4j(topic = "UserController")
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserDb userDb;

    public UserController(
            UserDb userDb) {
        this.userDb = userDb;
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestParam UUID userUuid) {
        try {
            userDb.save(new User(userUuid));
            log.info("Created user");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch(Exception e){
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
