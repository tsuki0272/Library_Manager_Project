package ca.umanitoba.cs.longkuma.logic.exceptions;

public class InvalidBookingDurationException extends Exception {
    public InvalidBookingDurationException(String message) {
        super(message);
    }
}