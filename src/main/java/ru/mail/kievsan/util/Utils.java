package ru.mail.kievsan.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ru.mail.kievsan.backend.security.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.PatternSyntaxException;

public class Utils {

    static private final Gson gson = new Gson();
    static private final ObjectMapper mapper = new ObjectMapper();


    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static String toJackson(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    public static String capitalize(String name) throws RuntimeException {
        String first = name.substring(0, 1);
        return name.replaceFirst(first, first.toUpperCase());
    }

    public static void verifyString(String string1, String string2) {
        verifyBCrypt(string1, string2, null);
    }

    public static void verifyString(String string1, String string2, String errMsg) throws NoSuchElementException {
        errMsg = errMsg == null || errMsg.isBlank() ? "Fail!" : errMsg;
        if (!Objects.equals(string1, string2)) {
            throw new NoSuchElementException(errMsg);
        }
    }

    public static void verifyBCrypt(String string, String hashCode) {
        verifyBCrypt(string, hashCode, null);
    }

    public static void verifyBCrypt(String string, String hashCode, String errMsg) throws NoSuchElementException {
        errMsg = errMsg == null || errMsg.isBlank() ? "Fail!" : errMsg;
        if (!PasswordEncoder.verifyBCrypt(string, hashCode)) {
            throw new NoSuchElementException(errMsg);
        }
    }

}
