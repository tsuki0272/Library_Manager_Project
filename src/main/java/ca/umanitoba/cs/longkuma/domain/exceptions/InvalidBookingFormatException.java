package ca.umanitoba.cs.longkuma.domain.exceptions;

public class InvalidBookingFormatException extends Exception {
    public InvalidBookingFormatException(String message) {
        super(message);
    }
}