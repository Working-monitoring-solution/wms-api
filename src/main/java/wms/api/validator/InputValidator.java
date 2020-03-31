package wms.api.validator;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import wms.api.exception.WMSException;
import wms.api.util.WMSCode;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class InputValidator {

    private final static Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public void validateEmptyField(String field, String value) throws WMSException {
        if (StringUtils.isEmpty(value))
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
    }

    public void validateEmptyField(String field, String value, String message)
            throws WMSException {
        if (StringUtils.isEmpty(value))
            throw new WMSException.NoDataInputException(field, message);
    }

    public void validateEmptyField(String field, Integer value) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
    }

    public void validateEmptyField(String field, Integer value, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
    }

    public void validateEmptyField(String field, Long value) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
    }

    public void validateEmptyField(String field, Long value, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
    }

    public void validateEmptyField(String field, Short value) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
    }

    public void validateEmptyField(String field, Short value, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
    }

    public void validateEmptyField(String field, Double value) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
    }

    public void validateEmptyField(String field, Double value, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
    }

    public void validateEmptyField(String field, Float value) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
    }

    public void validateEmptyField(String field, Float value, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
    }

    public void validateEmptyCollection(String field, Collection<?> collection)
            throws WMSException {
        if (CollectionUtils.isEmpty(collection))
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
    }

    public void validateEmptyCollection(String field, Collection<?> collection, String message)
            throws WMSException {
        if (CollectionUtils.isEmpty(collection))
            throw new WMSException.NoDataInputException(field, message);
    }

    public void validateIntegerField(String field, String value) throws WMSException {
        char[] chars = value.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c) || c == '_' || c == ',')
                continue;
            throw new WMSException.NumberFormatException(field);
        }
    }

    public void validateIntegerField(String field, String value, String message)
            throws WMSException {
        char[] chars = value.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c) || c == '_' || c == ',')
                continue;
            throw new WMSException.NumberFormatException(field, message);
        }
    }

    public void validateFloatField(String field, String value) throws WMSException {
        char[] chars = value.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c) || c == '_' || c == ',' || c == '.')
                continue;
            throw new WMSException.NumberFormatException(field);
        }
    }

    public void validateFloatField(String field, String value, String message)
            throws WMSException {
        char[] chars = value.toCharArray();
        for (char c : chars) {
            if (Character.isDigit(c) || c == '_' || c == ',' || c == '.')
                continue;
            throw new WMSException.NumberFormatException(field, message);
        }
    }

    public Date validateDateTimeField(String field, String value, SimpleDateFormat dateFormat)
            throws WMSException {
        if (StringUtils.isEmpty(value))
            throw new WMSException.NoDataInputException(field, WMSCode.INVALID_INPUT_CODE);
        try {
            return dateFormat.parse(value);
        } catch (Exception e) {
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_CODE);
        }
    }

    public Date validateDateTimeField(String field, String value, SimpleDateFormat dateFormat,
                                      String message)
            throws WMSException {
        if (StringUtils.isEmpty(value))
            throw new WMSException.NoDataInputException(field, message);
        try {
            return dateFormat.parse(value);
        } catch (Exception e) {
            throw new WMSException.DateTimeFormatException(field);
        }
    }

    public void validateEmailField(String field, String value) throws WMSException {
        if (StringUtils.isEmpty(value))
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        Matcher matcher = EMAIL_PATTERN.matcher(value.trim());
        if (matcher.matches())
            return;
        throw new WMSException.EmailFormatException(field);
    }

    public void validateEmailField(String field, String value, String message)
            throws WMSException {
        if (StringUtils.isEmpty(value))
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        Matcher matcher = EMAIL_PATTERN.matcher(value.trim());
        if (matcher.matches())
            return;
        throw new WMSException.EmailFormatException(field, message);
    }

    public void validateRangeField(String field, Long value, long min, long max)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        long data = value.longValue();
        if (data < min || data > max)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateRangeField(String field, Long value, long min, long max, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        long data = value.longValue();
        if (data < min || data > max)
            throw new WMSException.InvalidInputException(field, message);
    }

    public void validateRangeField(String field, Double value, double min, double max,
                                   String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        long data = value.longValue();
        if (data < min || data > max)
            throw new WMSException.InvalidInputException(field, message);
    }

    public void validateRangeField(String field, Double value, double min, double max)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        long data = value.longValue();
        if (data < min || data > max)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateRangeField(String field, Integer value, int min, int max)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        long data = value.longValue();
        if (data < min || data > max)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateRangeField(String field, Integer value, int min, int max, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        long data = value.longValue();
        if (data < min || data > max)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateRangeField(String field, Short value, short min, short max)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        long data = value.longValue();
        if (data < min || data > max)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateRangeField(String field, Short value, short min, short max, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        long data = value.longValue();
        if (data < min || data > max)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateMinField(String field, Long value, long min) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        long data = value.longValue();
        if (data < min)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateMinField(String field, Long value, long min, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        long data = value.longValue();
        if (data < min)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateMinField(String field, Integer value, int min) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        long data = value.longValue();
        if (data < min)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateMinField(String field, Integer value, int min, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        long data = value.longValue();
        if (data < min)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateMinField(String field, Short value, short min) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        long data = value.longValue();
        if (data < min)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateMinField(String field, Short value, short min, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        long data = value.longValue();
        if (data < min)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateMinField(String field, Date value, Date min) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        if (min.after(value))
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateMinField(String field, Date value, Date min, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        if (min.after(value))
            throw new WMSException.InvalidInputException(field, message);
    }

    public void validateMinLengthField(String field, String value, int minLength)
            throws WMSException {
        if (value.length() >= minLength)
            return;
        throw new WMSException.InvalidInputException(field, "Invalid Min Length (" + minLength + ")");
    }

    public void validateMinLengthField(String field, String value, int minLength, String message)
            throws WMSException {
        if (value.length() >= minLength)
            return;
        throw new WMSException.InvalidInputException(field, message);
    }

    public void validateMaxField(String field, Long value, long max) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        long data = value.longValue();
        if (data > max)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateMaxField(String field, Long value, long max, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        long data = value.longValue();
        if (data > max)
            throw new WMSException.InvalidInputException(field, message);
    }

    public void validateMaxField(String field, Integer value, int max) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        long data = value.longValue();
        if (data > max)
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateMaxField(String field, Integer value, int max, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        long data = value.longValue();
        if (data > max)
            throw new WMSException.InvalidInputException(field, message);
    }

    public void validateMaxField(String field, Short value, short max) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        long data = value.longValue();
        if (data > max)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
    }

    public void validateMaxField(String field, Short value, short max, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        long data = value.longValue();
        if (data > max)
            throw new WMSException.NoDataInputException(field, message);
    }

    public void validateMaxField(String field, Date value, Date max) throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        if (value.after(max))
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateMaxField(String field, Date value, Date max, String message)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        if (value.after(max))
            throw new WMSException.InvalidInputException(field, message);
    }

    public void validateLengthInRange(String field, String value, int minLength, int maxLength)
            throws WMSException {
        if (minLength <= value.length() && value.length() <= maxLength)
            return;
        throw new WMSException.InvalidInputException(field, "Invalid  Length");
    }

    public void validateMaxLengthField(String field, String value, int maxLength)
            throws WMSException {
        if (value.length() <= maxLength)
            return;
        throw new WMSException.InvalidInputException(field, "Invalid Max Length (" + maxLength + ")");
    }

    public void validateMaxLengthField(String field, String value, int maxLength, String message)
            throws WMSException {
        if (value.length() <= maxLength)
            return;
        throw new WMSException.InvalidInputException(field, message);
    }

    public void validateSomeValuesField(String field, String value, String... values)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        for (String ele : values) {
            if (value.equals(ele))
                return;
        }
        throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateSomeValuesField(String field, String value, String message,
                                        String... caseValues)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        for (String ele : caseValues) {
            if (value.equals(ele))
                return;
        }
        throw new WMSException.InvalidInputException(field, message);
    }

    public void validateSomeValuesField(String field, Long value, long... values)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        long l = value.longValue();
        for (long ele : values) {
            if (l == ele)
                return;
        }
        throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateSomeValuesField(String field, Long value, String message,
                                        long... caseValues)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        long l = value.longValue();
        for (long ele : caseValues) {
            if (l == ele)
                return;
        }
        throw new WMSException.InvalidInputException(field, message);
    }

    public void validateSomeValuesField(String field, Character value, char... values)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        char temp = value.charValue();
        for (char ele : values) {
            if (ele == temp)
                return;
        }
        throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateSomeValuesField(String field, Character value, String message,
                                        char... caseValues)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        char temp = value.charValue();
        for (char ele : caseValues) {
            if (ele == temp)
                return;
        }
        throw new WMSException.InvalidInputException(field, message);
    }

    public void validateSomeValuesField(String field, Integer value, int... values)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        int temp = value.intValue();
        for (int ele : values) {
            if (ele == temp)
                return;
        }
        throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateSomeValuesField(String field, Integer value, String message,
                                        int... caseValues)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        int temp = value.intValue();
        for (int ele : caseValues) {
            if (ele == temp)
                return;
        }
        throw new WMSException.InvalidInputException(field, message);
    }

    public void validateSomeValuesField(String field, Short value, short... values)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, WMSCode.NO_DATA_INPUT_MESSAGE);
        short temp = value.shortValue();
        for (short ele : values) {
            if (ele == temp)
                return;
        }
        throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
    }

    public void validateSomeValuesField(String field, Short value, String message,
                                        short... caseValues)
            throws WMSException {
        if (value == null)
            throw new WMSException.NoDataInputException(field, message);
        short temp = value.shortValue();
        for (short ele : caseValues) {
            if (ele == temp)
                return;
        }
        throw new WMSException.InvalidInputException(field, message);
    }

    public static void validateSpecialCharacter(String text) {
        if (validSpecialCharacter(text)) {
            throw new WMSException.InvalidInputException();
        }
    }

    public static void validateSpecialCharacter(String field, String text) {
        if (validSpecialCharacter(text)) {
            throw new WMSException.InvalidInputException(field, WMSCode.INVALID_INPUT_MESSAGE);
        }
    }

    public static boolean validSpecialCharacter(String input) {
        Pattern pattern = Pattern.compile(".*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?\\s+].*");
        if (pattern.matcher(input).matches()) {
            return true;
        }
        return false;
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
