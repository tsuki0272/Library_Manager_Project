package ca.umanitoba.cs.longkuma.logic.resource;

import ca.umanitoba.cs.longkuma.domain.exceptions.InvalidDateException;
import ca.umanitoba.cs.longkuma.domain.exceptions.InvalidMemberException;
import ca.umanitoba.cs.longkuma.domain.exceptions.InvalidTimeFormatException;
import ca.umanitoba.cs.longkuma.domain.member.Member;
import ca.umanitoba.cs.longkuma.domain.resource.Booking;
import ca.umanitoba.cs.longkuma.domain.resource.Resource;

public class BookingLogic {

    /*
     * Creates a booking for a member at a resource with specified date and time
     * Parses and validates date and time strings, builds the booking, and adds it to both resource and member
     *
     * @param member The member making the booking
     * @param resource The resource being booked
     * @param dateString The date string in DD/MM/YY format
     * @param timeString The time string in HH:MM-HH:MM format
     * @return The created Booking object
     * @throws InvalidDateException if the date format is invalid
     * @throws InvalidTimeFormatException if the time format is invalid
     * @throws InvalidMemberException if the member is invalid
     */
    public static Booking createBooking(Member member, Resource resource,
                                        String dateString, String timeString) throws InvalidDateException, InvalidTimeFormatException, InvalidMemberException {

        int[] dateParts = parseAndValidateDate(dateString);
        String[] timeParts = parseAndValidateTime(timeString);

        Booking booking = new Booking.BookingBuilder()
                .member(member)
                .startTime(timeParts[0])
                .endTime(timeParts[1])
                .day(dateParts[0])
                .month(dateParts[1])
                .year(dateParts[2])
                .build();

        resource.addBooking(booking);
        member.addBookedResource(resource);

        return booking;

    }

    /*
     * Parses and validates a date string in DD/MM/YY format
     * Checks format, separator positions, and value ranges for day, month, and year
     *
     * @param date The date string to parse in DD/MM/YY format
     * @return An integer array containing [day, month, year] where year is converted to full four-digit format
     * @throws InvalidDateException if date is null, wrong length, has invalid format, or contains out-of-range values
     */
    public static int[] parseAndValidateDate(String date) throws InvalidDateException {
        if (date == null || date.length() != 8)
            throw new InvalidDateException("Date must be DD/MM/YY");

        if (date.charAt(2) != '/' || date.charAt(5) != '/')
            throw new InvalidDateException("Date must be DD/MM/YY with slashes");

        try {
            int day = Integer.parseInt(date.substring(0, 2));
            int month = Integer.parseInt(date.substring(3, 5));
            int year = Integer.parseInt(date.substring(6, 8)) + 2000;

            if (day < 1 || day > 31) throw new InvalidDateException("Day out of range");
            if (month < 1 || month > 12) throw new InvalidDateException("Month out of range");
            if (year <= 2024) throw new InvalidDateException("Year must be 2025+");

            return new int[]{day, month, year};
        } catch (NumberFormatException e) {
            throw new InvalidDateException("Invalid date digits");
        }
    }

    /*
     * Parses and validates a time range string in HH:MM-HH:MM format
     * Checks format and separator positions
     *
     * @param time The time string to parse in HH:MM-HH:MM format
     * @return A string array containing [startTime, endTime] in HH:MM format
     * @throws InvalidTimeFormatException if time is null, wrong length, or has invalid format
     */
    public static String[] parseAndValidateTime(String time) throws InvalidTimeFormatException {
        if (time == null || time.length() != 11)
            throw new InvalidTimeFormatException("Time must be HH:MM-HH:MM");

        if (time.charAt(2) != ':' || time.charAt(5) != '-' || time.charAt(8) != ':')
            throw new InvalidTimeFormatException("Bad time format");

        String start = time.substring(0, 5);
        String end = time.substring(6, 11);

        return new String[]{start, end};
    }
}