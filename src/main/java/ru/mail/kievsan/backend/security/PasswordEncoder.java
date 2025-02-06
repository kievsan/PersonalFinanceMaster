package ru.mail.kievsan.backend.security;

import at.favre.lib.crypto.bcrypt.BCrypt;


public class PasswordEncoder {

    public String encodeBCrypt( String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    // $2a$12$US00g/uMhoSBm.HiuieBjeMtoN69SN.GE25fCpldebzkryUyopws6

    public boolean verifyBCrypt(String password, String hashCode) {
        return BCrypt.verifyer().verify(password.toCharArray(), hashCode).verified;
    }
}
