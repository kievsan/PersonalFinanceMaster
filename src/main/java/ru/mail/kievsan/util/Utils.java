package ru.mail.kievsan.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.mail.kievsan.backend.security.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Objects;


public class Utils {

    static private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            //.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();
    static public final ObjectMapper prettyJackson = new ObjectMapper()
                // для игнорирования неизвестных полей в JSON:
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .enable(SerializationFeature.INDENT_OUTPUT) // — для форматированного (многострочного) вывода
                .registerModule(new JavaTimeModule());      // - для типов даты и времени
    static private final ObjectMapper mapper = new ObjectMapper();

    static  {
        mapper  // для игнорирования неизвестных полей в JSON:
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new JavaTimeModule());     // - для типов даты и времени
    }

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
