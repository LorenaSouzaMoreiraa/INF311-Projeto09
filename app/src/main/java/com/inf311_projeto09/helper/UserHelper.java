package com.inf311_projeto09.helper;

public class UserHelper {

    private UserHelper() {
        //
    }

    public static boolean validateCPF(final String cpf) {
        if (cpf == null) return false;

        final String cleanedCPF = cpf.replaceAll("\\D", "");

        if (cleanedCPF.length() != 11) return false;
        if (cleanedCPF.matches("(\\d)\\1{10}")) return false;

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (cleanedCPF.charAt(i) - '0') * (10 - i);
        }
        int firstCheckDigit = 11 - (sum % 11);
        if (firstCheckDigit >= 10) firstCheckDigit = 0;

        if (firstCheckDigit != (cleanedCPF.charAt(9) - '0')) return false;

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (cleanedCPF.charAt(i) - '0') * (11 - i);
        }
        int secondCheckDigit = 11 - (sum % 11);
        if (secondCheckDigit >= 10) secondCheckDigit = 0;

        return secondCheckDigit == (cleanedCPF.charAt(10) - '0');
    }
}
