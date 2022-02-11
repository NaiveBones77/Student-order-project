package edu.studentorder.exception;

public class TransportException extends Exception {

    private String code;

    public TransportException(String message) {
        super(message);
    }

    public TransportException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() {
        return code;
    }

}
