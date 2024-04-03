package dev.distributed.service.workers;

import dev.distributed.service.database.UserDb;
import dev.distributed.service.entities.User;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@NoArgsConstructor
@Service
public class UserService {

    private UserDb userDb;

    @Autowired
    public UserService(UserDb userDb) {
        this.userDb = userDb;
    }

    public User resolveUser(UUID userId) {
        return this.userDb.findById(userId).orElse(null);
    }


}

