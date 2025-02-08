package ru.mail.kievsan.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import ru.mail.kievsan.backend.exception.NotValidUserException;
import ru.mail.kievsan.backend.model.Identity;
import ru.mail.kievsan.backend.model.Role;

import java.util.Objects;


@Getter
public class User extends Identity<String> {

    public static final int MIN_LOGIN_LENGTH = 3;
    public static final int MAX_LOGIN_LENGTH = 20;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 60;

    private String password;
    private final Role role;

    @Builder
    @JsonCreator()
    @JsonPropertyOrder({ "id", "password", "role" })
    public User(@JsonProperty("id") String id,
                 @JsonProperty("password") String password,
                 @JsonProperty("role") Role role) {
        super(id);
        setPassword(password);
        this.role = role;
    }

    public static boolean isNotValidLogin(String login) {
        String regex1 = "^(?=.*[a-z])";     // Хотя бы одна маленькая латинская буква
        String regex11 = "(?=.*\\d)";       // Хотя бы одна цифра
        String regex2 = "[A-Za-z\\d_@]";    // Только цифры, латинские буквы, подчеркивание или @
        String regex3 = String.format("{%d,%d}", MIN_LOGIN_LENGTH, MAX_LOGIN_LENGTH); // Длина в интервале от MIN до MAX
        String regex =regex1 + regex2 + regex3 + "$";
        return !login.matches(regex);
    }

    public static boolean isNotValidPassword(String password) {
        return password.isBlank() || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH;
    }

    public void setPassword(String password) throws NotValidUserException {
        if (isNotValidPassword(password)) throw new NotValidUserException("Not valid user password!");
        this.password = password;
    }

    @Override
    public void validateId() throws NotValidUserException {
        if (isNotValidLogin(id)) throw new NotValidUserException("Not valid user login!");
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
