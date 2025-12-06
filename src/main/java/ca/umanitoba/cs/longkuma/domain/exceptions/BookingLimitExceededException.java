package ca.umanitoba.cs.longkuma.domain.exceptions;

public class BookingLimitExceededException extends Exception {
    public BookingLimitExceededException(String message) {
        super(message);
    }
}