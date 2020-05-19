package wms.api.exception;

import lombok.Getter;
import lombok.Setter;
import wms.api.util.WMSCode;

@Getter
@Setter
public class WMSException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    private String field;

    public WMSException(String code) {
        super();
        this.code = code;
    }

    public WMSException(String code, String message) {
        super(message);
        this.code = code;
    }

    public WMSException(String code, String field, String message) {
        super(field + ": " + message);
        this.code = code;
        this.field = field;
    }

    public static class AuthenticationErrorException extends WMSException {

        private static final long serialVersionUID = -7257749122280775814L;

        public AuthenticationErrorException() {
            super(WMSCode.AUTHENTICATION_ERROR_CODE, WMSCode.AUTHENTICATION_ERROR_MESSAGE);
        }
    }

    public static class AuthorizationErrorException extends WMSException {

        private static final long serialVersionUID = -7257749122280775898L;

        public AuthorizationErrorException() {
            super(WMSCode.AUTHORIZATION_ERROR_CODE, WMSCode.AUTHORIZATION_ERROR_MESSAGE);
        }
    }

    public static class EmailExistException extends WMSException {

        private static final long serialVersionUID = -7257749122280775812L;

        public EmailExistException() {
            super(WMSCode.EMAIL_EXIST_CODE, WMSCode.EMAIL_EXIST_MESSAGE);
        }

    }

    public static class InvalidInputException extends WMSException {

        private static final long serialVersionUID = -7257749122280775815L;

        public InvalidInputException() {
            super(WMSCode.INVALID_INPUT_CODE, WMSCode.INVALID_INPUT_MESSAGE);
        }

        public InvalidInputException(String field) {
            super(WMSCode.INVALID_INPUT_CODE, field, WMSCode.INVALID_INPUT_MESSAGE);
        }

        public InvalidInputException(String field, String message) {
            super(WMSCode.INVALID_INPUT_CODE, field, message);
        }

    }

    public static class NotActiveEntityException extends WMSException {

        private static final long serialVersionUID = -4657301284704548008L;

        public NotActiveEntityException(String field) {
            super(WMSCode.NOT_ACTIVE_ENTITY_CODE, field, WMSCode.NOT_ACTIVE_ENTITY_MESSAGE);
        }
    }

    public static class NotFoundEntityException extends WMSException {

        private static final long serialVersionUID = -4657301284704548008L;

        public NotFoundEntityException() {
            super(WMSCode.NOT_FOUND_ENTITY_CODE, WMSCode.NOT_FOUND_ENTITY_MESSAGE);
        }

        public NotFoundEntityException(String field) {
            super(WMSCode.NOT_FOUND_ENTITY_CODE, field, WMSCode.NOT_FOUND_ENTITY_MESSAGE);
        }

    }

    public static class DuplicateEntityException extends WMSException {

        private static final long serialVersionUID = -4657301284704548009L;

        public DuplicateEntityException() {
            super(WMSCode.NOT_FOUND_ENTITY_CODE, WMSCode.NOT_FOUND_ENTITY_MESSAGE);
        }

        public DuplicateEntityException(String field) {
            super(WMSCode.NOT_FOUND_ENTITY_CODE, field, WMSCode.NOT_FOUND_ENTITY_MESSAGE);
        }

    }

    public static class UnknownException extends WMSException {

        private static final long serialVersionUID = -7257749122280775815L;

        public UnknownException() {
            super(WMSCode.UNKNOWN_ERROR, WMSCode.UNKNOWN_ERROR_MESSAGE);
        }

    }
}
