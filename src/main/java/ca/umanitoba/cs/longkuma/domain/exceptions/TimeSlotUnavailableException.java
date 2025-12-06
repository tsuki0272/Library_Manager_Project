package ca.umanitoba.cs.longkuma.domain.exceptions;

public class TimeSlotUnavailableException extends Exception {
    public TimeSlotUnavailableException(String message) {
        super(message);
    }
}