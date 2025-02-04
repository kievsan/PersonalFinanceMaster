package ru.mail.kievsan.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.mail.kievsan.backend.model.Identity;
import ru.mail.kievsan.backend.model.Role;

import java.util.Objects;


@Getter
public class User extends Identity<String> {

    @Setter
    private String password;

    private final Role role;

    @Builder
    @JsonCreator()
    public User(@JsonProperty("id") String id,
                 @JsonProperty("password") String password,
                 @JsonProperty("role") Role role) {
        super(id);
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return role.name() + " '" + id + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), password, role);
    }
}
