package ru.mail.kievsan.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import ru.mail.kievsan.backend.exception.NotValidBudgetCategoryException;
import ru.mail.kievsan.backend.model.Identity;
import ru.mail.kievsan.util.Utils;

import java.util.Objects;


@Getter
public class BudgetCategory extends Identity<String> {

    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 30;
    public static final int UUID_LENGTH = 36;

    private final String userId;
    private final boolean system;
    private String name;
    private double limit;


    public BudgetCategory(String userId, boolean system, String name, double limit) {
        this(Utils.randomUUID(), userId, system, name, limit);
    }

    public BudgetCategory(BudgetCategory category) {
        this(Utils.randomUUID(), category.userId, category.system, category.name, category.limit);
    }

    @Builder
    @JsonCreator()
    @JsonPropertyOrder({ "id", "userId", "system", "name", "limit" })
    public BudgetCategory(String id, String userId, boolean system, String name, double limit) {
        super(id);
//        validateUserId();
        this.userId = userId;
        this.system = system;
        setName(name);
        setLimit(limit);
    }

    public BudgetCategory copy(BudgetCategory category) {
        return new BudgetCategory(category.id, category.userId, category.system, category.name, category.limit);
    }


    public static boolean isNotValidId(String id) {
        return id == null || id.isBlank() || id.length() != UUID_LENGTH;
    }

//    public static boolean isNotValidUserId(String userId) {
//        return User.isNotValidLogin(userId);
//    }

    public static boolean isNotValidName(String name) {
        return name == null || name.isBlank() || name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public void setLimit(double limit) {
        this.limit = Math.abs(limit);
    }

    @Override
    public void validateId() {
        if (isNotValidId(id)) throw new NotValidBudgetCategoryException("Not valid budget category id!");
    }

//    public void validateUserId() {
//        if (isNotValidUserId(id)) throw new NotValidUserException("Not valid category id!");
//    }

    public void validateName(String name) {
        if (isNotValidName(name)) throw new NotValidBudgetCategoryException("Not valid budget category name!");
    }

    @Override
    public String toString() {
        return String.format("Бюджетная категория '%s' с лимитом %s руб. для пользователя '%s'", name, limit, userId);
    }
    public String toMiddleString() {
        return "Бюдж.кат. '" + name + "' (лимит " + limit + " руб.)";
    }
    public String toShortString() {
        return "'" + name + "' ( " + limit + " руб. )";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BudgetCategory category = (BudgetCategory) o;
        return Objects.equals(getId(), category.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name);
    }

//    public int compareTo(BudgetCategory category){
//        return name.compareTo(category.getName());
//    }

}
