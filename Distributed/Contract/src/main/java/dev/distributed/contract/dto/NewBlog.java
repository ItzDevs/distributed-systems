package dev.distributed.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;


/**
 * Data Transfer Object for creating a new blog.
*/
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewBlog implements Serializable {

    public String title;

    public String content;

    public String[] mediaUrls;

    public String[] tags;

    public UUID authorId;
}
