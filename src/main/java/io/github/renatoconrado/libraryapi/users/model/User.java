package io.github.renatoconrado.libraryapi.users.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Data
@Table(schema = "public")
public @Entity class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Type(ListArrayType.class)
    @Column(columnDefinition = "varchar[]")
    private List<String> roles;

    public User(String login, String email, String password, List<String> roles) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User() {}
}
