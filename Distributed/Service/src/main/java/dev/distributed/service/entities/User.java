package dev.distributed.service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    private UUID id;

    @OneToMany(mappedBy = "id")
    private Set<Blog> blogs;
}