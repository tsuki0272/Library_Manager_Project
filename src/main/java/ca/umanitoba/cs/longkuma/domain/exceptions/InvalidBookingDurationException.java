package ca.umanitoba.cs.longkuma.domain.exceptions;

public class InvalidBookingDurationException extends Exception {
    public InvalidBookingDurationException(String message) {
        super(message);
    }
}