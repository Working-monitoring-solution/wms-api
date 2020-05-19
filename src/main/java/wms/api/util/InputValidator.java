package wms.api.util;

import wms.api.exception.WMSException;
import wms.api.util.WMSCode;

import java.util.regex.Pattern;

public class InputValidator {

    private final static Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public static boolean validSpecialCharacter(String input) {
        Pattern pattern = Pattern.compile(".*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?\\s+].*");
        if (pattern.matcher(input).matches()) {
            return true;
        }
        return false;
    }

    public static boolean validateEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static void validateLetterNumberAndSpace(String field, String text) {
        if (!validLetterNumberAndSpace(text)) {
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
        }
    }

    public static boolean validLetterNumberAndSpace(String input) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9\\s]+");
        return pattern.matcher(input).matches();
    }

    public static void validateLetterAndNumber(String field, String text) {
        if (!validLetterAndNumber(text)) {
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
        }
    }

    public static boolean validLetterAndNumber(String input) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+");
        return pattern.matcher(input).matches();
    }
}
