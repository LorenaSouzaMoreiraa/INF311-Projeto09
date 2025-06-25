package com.example.inf311_projeto09.helper;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordHelper {

    private PasswordHelper() {
        //
    }

    public static String hashPassword(final String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    public static boolean verifyPassword(final String plainPassword, final String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        final BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}
