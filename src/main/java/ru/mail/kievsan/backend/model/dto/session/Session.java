package ru.mail.kievsan.backend.model.dto.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import ru.mail.kievsan.backend.model.Role;
import ru.mail.kievsan.backend.model.entity.User;

import java.util.Scanner;

@Getter
@Setter
@AllArgsConstructor
public class Session {
    Scanner scanner;
    User currentUser;
}
