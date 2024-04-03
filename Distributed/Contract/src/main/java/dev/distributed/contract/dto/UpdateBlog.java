package dev.distributed.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

// Extends the NewBlog DTO as each field may contain an update, however the only difference
// between posting a new Blog and updating a blog is the id.
/**
 * The Data Transfer Object for updating a blog.
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBlog extends NewBlog implements Serializable {

    public UpdateBlog(UUID id, String title, String content, String[] mediaUrls, String[] tags, UUID authorId) {
        super(title, content, mediaUrls, tags, authorId);
        this.id = id;
    }
    private UUID id;
}
