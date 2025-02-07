package ru.mail.kievsan.util;

import ru.mail.kievsan.backend.security.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Objects;

public class Utils {

    public static String capitalize(String name) {
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
