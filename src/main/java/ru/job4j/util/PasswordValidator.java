package ru.job4j.util;

import java.util.regex.Pattern;

public final class PasswordValidator {
    /**
     * 8-16 characters password with at least one digit, at least one
     * lowercase letter, at least one uppercase letter, at least one
     * special character with no white spaces
     */
    private static final String PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    private PasswordValidator() {
    }

    public static boolean validate(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static String invalidMsg() {
        return "Password must be:"
                + "8-16 characters password with at least one digit, at least one"
                + "lowercase letter, at least one uppercase letter, at least one"
                + "special character with no white spaces";
    }
}
