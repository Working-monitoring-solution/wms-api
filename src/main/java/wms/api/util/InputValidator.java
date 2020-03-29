package wms.api.util;

import org.springframework.stereotype.Component;
import wms.api.exception.WMSException;

@Component
public class InputValidator {

    public void validateLongValue(String id) {
        boolean isNumeric = id.matches("\\d+");
        if (!isNumeric) {
            throw new WMSException.InvalidInputException();
        }
    }
}
