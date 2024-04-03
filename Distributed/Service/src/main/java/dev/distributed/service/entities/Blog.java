package dev.distributed.service.entities;

import dev.distributed.contract.TimestampHelper;
import dev.distributed.contract.dto.NewBlog;
import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Blog")
@Table(name = "blogs", indexes = {
        @Index(name = "title_unq__indx", columnList = "title", unique = true),
})
@Getter @Setter
public class Blog {

    @Id
    private UUID id;

    private String title;

    private String content;

    @Type(StringArrayType.class)
    @Column(columnDefinition = "TEXT[]")
    private String[] mediaUrls;

    @Type(StringArrayType.class)
    @Column(columnDefinition = "TEXT[]")
    private String[] tags;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    public static Blog toBlog(NewBlog blog, User resolvedUser){
        return new Blog(
                UUID.randomUUID(),
                blog.getTitle(),
                blog.getContent(),
                blog.getMediaUrls(),
                blog.getTags(),
                resolvedUser,
                TimestampHelper.getCurrentTimestamp(),
                TimestampHelper.getCurrentTimestamp());
    }


    public static boolean isSame(Blog blog, NewBlog newBlog){
        return blog.title.equals(newBlog.getTitle()) &&
                blog.content.equals(newBlog.getContent()) &&
                Arrays.equals(blog.mediaUrls, newBlog.getMediaUrls()) &&
                Arrays.equals(blog.tags, newBlog.getTags());

    }
}


