package ru.mail.kievsan.backend.model.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ru.mail.kievsan.backend.model.entity.User;

import java.util.Scanner;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    Scanner scanner;
    User currentUser;
}
