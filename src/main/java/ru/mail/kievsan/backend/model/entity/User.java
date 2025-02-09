package ru.mail.kievsan.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.mail.kievsan.backend.exception.NotValidUserException;
import ru.mail.kievsan.backend.model.ActivityStatus;
import ru.mail.kievsan.backend.model.Identity;
import ru.mail.kievsan.backend.model.Role;

import java.time.LocalDate;
import java.util.Objects;


@Getter
public class User extends Identity<String> {

    public static final int MIN_LOGIN_LENGTH = 3;
    public static final int MAX_LOGIN_LENGTH = 20;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 60;

    private String password;
    private final Role role;
    private final LocalDate reg_date;
    @Setter
    private ActivityStatus status;
    @Getter
    private boolean del;


    public User(String id, String password) {
        this(id, password, null);
    }

    public User(String id, String password, Role role) {
        this(id, password, role, null);
    }

    public User(String id, String password, Role role, LocalDate reg_date) {
        this(id, password, role, reg_date, null);
    }

    public User(String id, String password, Role role, LocalDate reg_date, ActivityStatus status) {
        this(id, password, role, reg_date, status, false);
    }

    public User(User user, String password) {
        this(user.getId(), password, user.getRole(), user.getReg_date(), user.getStatus(), user.isDel());
    }

    @Builder
    @JsonCreator()
    @JsonPropertyOrder({ "id", "password", "role", "reg_date", "status" })
    public User(@JsonProperty("id") String id,
                @JsonProperty("password") String password,
                @JsonProperty("role") Role role,
                @JsonProperty("reg_date") LocalDate reg_date,
                @JsonProperty("status") ActivityStatus status,
                @JsonProperty("is_del") boolean isDeleted) {
        super(id);
        setPassword(password);
        this.role = role == null ? Role.USER : role;                  // (nullable = false)
        this.reg_date = reg_date == null ? LocalDate.now() : reg_date;  // (nullable = false)
        this.status = status == null ? ActivityStatus.ACTIVE : status;  // (nullable = false)
        this.del = isDeleted;
    }

    public static boolean isNotValidLogin(String login) {
        String regex = "^(?=.*[a-z])";     // Хотя бы одна маленькая латинская буква в начале
        String regex1 = "(?=.*\\d)";       // Хотя бы одна цифра
        String regex2 = "[A-Za-z\\d_@]";    // Только цифры, латинские буквы, подчеркивание или @
        String regex3 = String.format("{%d,%d}", MIN_LOGIN_LENGTH, MAX_LOGIN_LENGTH); // Длина в интервале от MIN до MAX
        regex += regex2 + regex3 + "$";
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

    public void setDel(boolean del) {
        this.del = del;
        if (del) setStatus(ActivityStatus.BLOCKED);
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
