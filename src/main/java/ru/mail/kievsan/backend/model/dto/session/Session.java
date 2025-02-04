package ru.mail.kievsan.backend.model.dto.session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import ru.mail.kievsan.backend.model.Role;

import java.util.Scanner;

@Getter
@Setter
@AllArgsConstructor
public class SessionUser {
    Scanner scanner;
    String login;
    String password;
    Role role;
}
