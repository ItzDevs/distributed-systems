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
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Blog")
@Table(name = "blogs", indexes = {
        @Index(name = "title__indx", columnList = "title"),
})
@Getter @Setter
public class Blog {

    @Id
    public UUID id;

    public String title;

    public String content;

    @Type(StringArrayType.class)
    @Column(columnDefinition = "TEXT[]")
    public String[] mediaUrls;

    @Type(StringArrayType.class)
    @Column(columnDefinition = "TEXT[]")
    public String[] tags;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    public User author;

    public Timestamp createdAt;

    public Timestamp updatedAt;

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
}


