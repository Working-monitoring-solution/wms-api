package wms.api.util;

public class WMSCode {

    public static final String SUCCESS = "00";

    public static final String INVALID_INPUT_CODE = "01";
    public static final String INVALID_INPUT_MESSAGE = "Invalid Data Input";

    public static final String NOT_ACTIVE_ENTITY_CODE = "02";
    public static final String NOT_ACTIVE_ENTITY_MESSAGE = "Not Active";

    public static final String NOT_FOUND_ENTITY_CODE = "03";
    public static final String NOT_FOUND_ENTITY_MESSAGE = "Not Found Data";

    public static final String DUPLICATE_ENTITY_CODE = "04";
    public static final String DUPLICATE_ENTITY_MESSAGE = "Duplicate data";

    public static final String CONNECTION_TIMEOUT = "09";
    public static final String CONNECTION_TIMEOUT_DESCRIPTION = "Connection timeout!";

    public static final String UNKNOWN_ERROR = "99";
    public static final String UNKNOWN_ERROR_MESSAGE = "Internal errors";

    public static final String AUTHENTICATION_ERROR_CODE = "98";
    public static final String AUTHENTICATION_ERROR_MESSAGE = "Authentication error";

    public static final String EMAIL_EXIST_CODE = "96";
    public static final String EMAIL_EXIST_MESSAGE = "Email is used";

    public static final String AUTHORIZATION_ERROR_CODE = "94";
    public static final String AUTHORIZATION_ERROR_MESSAGE = "Authorization error";
}
