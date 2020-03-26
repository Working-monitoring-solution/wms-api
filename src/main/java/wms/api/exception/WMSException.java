package wms.api.exception;

import lombok.Getter;
import lombok.Setter;
import wms.api.util.WMSCode;

@Getter
@Setter
public class WMSException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String          code;

    private Object          data;

    public WMSException(String code) {
        super();
        this.code = code;
    }

    public WMSException(String code, String message) {
        super(message);
        this.code = code;
    }

    public WMSException(String code, Object data, String message) {
        super(data + ": " + message);
        this.code = code;
        this.data = data;
    }

    public static class AuthenticationErrorException extends WMSException {

        private static final long serialVersionUID = -7257749122280775814L;

        public AuthenticationErrorException() {
            super(WMSCode.AUTHENTICATION_ERROR_CODE, WMSCode.AUTHENTICATION_ERROR_MESSAGE);
        }
    }

    public static class UserNotActiveException extends WMSException {

        private static final long serialVersionUID = -7257749122280775813L;

        public UserNotActiveException() {
            super(WMSCode.USER_NOT_ACTIVE_CODE, WMSCode.USER_NOT_ACTIVE_MESSAGE);
        }

    }

    public static class EmailExistException extends WMSException {

        private static final long serialVersionUID = -7257749122280775812L;

        public EmailExistException() {
            super(WMSCode.EMAIL_EXIST_CODE, WMSCode.EMAIL_EXIST_MESSAGE);
        }

    }

    public static class NoDataInputException extends WMSException {

        private static final long serialVersionUID = -7257749122280775812L;

        public NoDataInputException() {
            super(WMSCode.NO_DATA_INPUT_CODE, WMSCode.NO_DATA_INPUT_MESSAGE);
        }

        public NoDataInputException(String message) {
            super(WMSCode.NO_DATA_INPUT_CODE, message);
        }

        public NoDataInputException(String field, String message) {
            super(WMSCode.NO_DATA_INPUT_CODE, field, message);
        }

    }

    public static class InvalidInputException extends WMSException {

        private static final long serialVersionUID = -7257749122280775815L;

        public InvalidInputException() {
            super(WMSCode.INVALID_INPUT_CODE, WMSCode.INVALID_INPUT_MESSAGE);
        }

        public InvalidInputException(String message) {
            super(WMSCode.INVALID_INPUT_CODE, message);
        }

        public InvalidInputException(String field, String message) {
            super(WMSCode.INVALID_INPUT_CODE, field, message);
        }

    }

    public static class NotActiveEntityException extends WMSException {

        private static final long serialVersionUID = -4657301284704548008L;

        public NotActiveEntityException() {
            super(WMSCode.NOT_ACTIVE_ENTITY_CODE, WMSCode.NOT_ACTIVE_ENTITY_MESSAGE);
        }

        public NotActiveEntityException(String message) {
            super(WMSCode.NOT_ACTIVE_ENTITY_CODE, message);
        }

        public NotActiveEntityException(String field, String message) {
            super(WMSCode.NOT_ACTIVE_ENTITY_CODE, field, message);
        }
    }

    public static class NotFoundEntityException extends WMSException {

        private static final long serialVersionUID = -4657301284704548008L;

        public NotFoundEntityException() {
            super(WMSCode.NOT_FOUND_ENTITY_CODE, WMSCode.NOT_FOUND_ENTITY_MESSAGE);
        }

        public NotFoundEntityException(String message) {
            super(WMSCode.NOT_FOUND_ENTITY_CODE, message);
        }

        public NotFoundEntityException(String field, String message) {
            super(WMSCode.NOT_FOUND_ENTITY_CODE, field, message);
        }

    }

    public static class DuplicatedEntityException extends WMSException {

        private static final long serialVersionUID = 6685802703164214179L;

        public DuplicatedEntityException() {
            super(WMSCode.DUPLICATE_ENTITY_CODE, WMSCode.DUPLICATE_ENTITY_MESSAGE);
        }

        public DuplicatedEntityException(String message) {
            super(WMSCode.DUPLICATE_ENTITY_CODE, message);
        }

        public DuplicatedEntityException(String field, String message) {
            super(WMSCode.DUPLICATE_ENTITY_CODE, field, message);
        }

    }

    public static class LockedEntityException extends WMSException {

        private static final long serialVersionUID = -4657301284704548008L;

        public LockedEntityException() {
            super(WMSCode.LOCKED_ENTITY_CODE, WMSCode.LOCKED_ENTITY_MESSAGE);
        }

        public LockedEntityException(String message) {
            super(WMSCode.LOCKED_ENTITY_CODE, message);
        }

        public LockedEntityException(String field, String message) {
            super(WMSCode.LOCKED_ENTITY_CODE, field, message);
        }


    }

    public static class UsedEntityException extends WMSException {

        private static final long serialVersionUID = -4657301284704548008L;

        public UsedEntityException() {
            super(WMSCode.USED_ENTITY_CODE, WMSCode.USED_ENTITY_MESSAGE);
        }

        public UsedEntityException(String message) {
            super(WMSCode.USED_ENTITY_CODE, message);
        }

        public UsedEntityException(String field, String message) {
            super(WMSCode.USED_ENTITY_CODE, field, message);
        }

    }

    public static class NotAvailableEntityException extends WMSException {

        private static final long serialVersionUID = -2085391098403067857L;

        public NotAvailableEntityException() {
            super(WMSCode.NOT_ACTIVE_ENTITY_CODE, WMSCode.NOT_AVAI_ENTITY_MESSAGE);
        }

        public NotAvailableEntityException(String message) {
            super(WMSCode.NOT_ACTIVE_ENTITY_CODE, message);
        }

        public NotAvailableEntityException(String field, String message) {
            super(WMSCode.NOT_ACTIVE_ENTITY_CODE, field, message);
        }

    }

    public static class FailedToExecuteException extends WMSException {

        private static final long serialVersionUID = 7641702942202000228L;


        public FailedToExecuteException() {
            super(WMSCode.FAILED_TO_EXECUTE, WMSCode.FAILED_TO_EXECUTE_DESCRIPTION);
        }

        public FailedToExecuteException(String message) {
            super(WMSCode.FAILED_TO_EXECUTE, message);
        }

        public FailedToExecuteException(String field, String message) {
            super(WMSCode.FAILED_TO_EXECUTE, field, message);
        }

    }

    public static class NumberFormatException extends WMSException {

        private static final long serialVersionUID = -4657301284704548008L;

        public NumberFormatException() {
            super(WMSCode.NUMBER_FORMAT_CODE, WMSCode.NUMBER_FORMAT_MESSAGE);
        }

        public NumberFormatException(String message) {
            super(WMSCode.NUMBER_FORMAT_CODE, message);
        }

        public NumberFormatException(String field, String message) {
            super(WMSCode.NUMBER_FORMAT_CODE, field, message);
        }
    }

    public static class EmailFormatException extends WMSException {

        private static final long serialVersionUID = -4657301284704548008L;

        public EmailFormatException() {
            super(WMSCode.EMAIL_FORMAT_CODE, WMSCode.EMAIL_FORMAT_MESSAGE);
        }

        public EmailFormatException(String message) {
            super(WMSCode.EMAIL_FORMAT_CODE, message);
        }

        public EmailFormatException(String field, String message) {
            super(WMSCode.EMAIL_FORMAT_CODE, field, message);
        }
    }

    public static class DateTimeFormatException extends WMSException {

        private static final long serialVersionUID = -4657301284704548008L;

        public DateTimeFormatException() {
            super(WMSCode.DATE_FORMAT_CODE, WMSCode.DATE_FORMAT_MESSAGE);
        }

        public DateTimeFormatException(String message) {
            super(WMSCode.DATE_FORMAT_CODE, message);
        }

        public DateTimeFormatException(String field, String message) {
            super(WMSCode.DATE_FORMAT_CODE, field, message);
        }
    }

    public static class AccessResourceException extends WMSException {

        private static final long serialVersionUID = -4657301284704548008L;

        public AccessResourceException() {
            super(WMSCode.ACCESS_RESOURCES_CODE, WMSCode.ACCESS_RESOURCES_MESSAGE);
        }

        public AccessResourceException(String message) {
            super(WMSCode.ACCESS_RESOURCES_CODE, message);
        }

        public AccessResourceException(String field, String message) {
            super(WMSCode.ACCESS_RESOURCES_CODE, field, message);
        }

    }

    public static class EmptyDataException extends WMSException {

        private static final long serialVersionUID = -1689519904270635214L;

        public EmptyDataException() {
            super(WMSCode.EMPTY_DATA_CODE, WMSCode.EMPTY_DATA_MESSAGE);
        }

        public EmptyDataException(String message) {
            super(WMSCode.EMPTY_DATA_CODE, message);
        }

        public EmptyDataException(String field, String message) {
            super(WMSCode.EMPTY_DATA_CODE, field, message);
        }

    }

    public static class UnknownException extends WMSException {

        private static final long serialVersionUID = -7257749122280775815L;

        public UnknownException() {
            super(WMSCode.UNKNOWN_ERROR, WMSCode.UNKNOWN_ERROR_MESSAGE);
        }

        public UnknownException(String message) {
            super(WMSCode.UNKNOWN_ERROR, message);
        }

        public UnknownException(String field, String message) {
            super(WMSCode.UNKNOWN_ERROR, field, message);
        }

    }
}
