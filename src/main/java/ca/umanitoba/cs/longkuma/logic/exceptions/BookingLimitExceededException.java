package ca.umanitoba.cs.longkuma.logic.exceptions;

public class BookingLimitExceededException extends Exception {
    public BookingLimitExceededException(String message) {
        super(message);
    }
}