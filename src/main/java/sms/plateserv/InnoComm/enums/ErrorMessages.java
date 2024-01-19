package sms.plateserv.InnoComm.enums;

public enum ErrorMessages {

    SUCCESS("200", "SUCCESS"),
    FAILURE("500", "FAILURE"),
    DATA_NOT_FOUND("5000", "Data not found "),
    SOURCE_INPUT_REQUIRE("5002", "input Fields %s  is required "),
    INVALID_DATA("5003", "Data not available for given %s %s and %s %s"),
    SERVICE_NOT_AVAILABLE("5004", "Selected Service not available , please select another slot. ");


    private final String code;
    private final String message;

    ErrorMessages(String errorCode, String errorMessage) {
        this.code = errorCode;
        this.message = errorMessage;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
