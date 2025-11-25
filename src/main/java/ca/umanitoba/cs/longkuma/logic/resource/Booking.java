package ca.umanitoba.cs.longkuma.logic.resource;

import ca.umanitoba.cs.longkuma.logic.exceptions.*;
import ca.umanitoba.cs.longkuma.logic.member.Member;
import com.google.common.base.Preconditions;

public class Booking {
    final private Member member;

    final private String startTime;
    final private String endTime;
    final private int day;
    final private int month;
    final private int year;

    private Booking(Member member, String startTime, String endTime, int day, int month, int year) {
        this.member = member;
        this.startTime = startTime;
        this.day = day;
        this.month = month;
        this.year = year;
        this.endTime = endTime;
        checkBooking();
    }

    public static class BookingBuilder {
        private Member member;
        private String startTime;
        private String endTime;
        private int day;
        private int month;
        private int year;

        public BookingBuilder() {}

        /**
         * Sets the member for the booking being built
         *
         * @param member The member making the booking
         * @return The BookingBuilder instance for method chaining
         * @throws InvalidMemberException if member is null
         */
        public BookingBuilder member(Member member) throws InvalidMemberException {
            if (member == null) {
                throw new InvalidMemberException("Member should not be null.");
            }
            this.member = member;
            return this;
        }

        /**
         * Sets the start time for the booking being built
         *
         * @param startTime The start time in "HH:MM" format
         * @return The BookingBuilder instance for method chaining
         * @throws InvalidTimeFormatException if startTime is null or not 5 characters
         */
        public BookingBuilder startTime(String startTime) throws InvalidTimeFormatException {
            if (startTime == null) {
                throw new InvalidTimeFormatException("Start time should not be null.");
            }
            if (startTime.length() != 5) {
                throw new InvalidTimeFormatException("Start time should have 5 characters (HH:MM).");
            }
            this.startTime = startTime;
            return this;
        }

        /**
         * Sets the end time for the booking being built
         *
         * @param endTime The end time in "HH:MM" format
         * @return The BookingBuilder instance for method chaining
         * @throws InvalidTimeFormatException if endTime is null or not 5 characters
         */
        public BookingBuilder endTime(String endTime) throws InvalidTimeFormatException {
            if (endTime == null) {
                throw new InvalidTimeFormatException("End time should not be null.");
            }
            if (endTime.length() != 5) {
                throw new InvalidTimeFormatException("End time should have 5 characters (HH:MM).");
            }
            this.endTime = endTime;
            return this;
        }

        /**
         * Sets the day for the booking being built
         *
         * @param day The day of the month (1-31)
         * @return The BookingBuilder instance for method chaining
         * @throws InvalidDateException if day is not between 1 and 31
         */
        public BookingBuilder day(int day) throws InvalidDateException {
            if (day < 1 || day > 31) {
                throw new InvalidDateException("Day should be between 1 and 31.");
            }
            this.day = day;
            return this;
        }

        /**
         * Sets the month for the booking being built
         *
         * @param month The month of the year (1-12)
         * @return The BookingBuilder instance for method chaining
         * @throws InvalidDateException if month is not between 1 and 12
         */
        public BookingBuilder month(int month) throws InvalidDateException {
            if (month < 1 || month > 12) {
                throw new InvalidDateException("Month should be between 1 and 12.");
            }
            this.month = month;
            return this;
        }

        /**
         * Sets the year for the booking being built
         *
         * @param year The year (must be 2025 or later)
         * @return The BookingBuilder instance for method chaining
         * @throws InvalidDateException if year is 2024 or earlier
         */
        public BookingBuilder year(int year) throws InvalidDateException {
            if (year <= 2024) {
                throw new InvalidDateException("Year should be 2025 or later.");
            }
            this.year = year;
            return this;
        }

        /**
         * Builds and returns a new Booking instance with the configured properties
         *
         * @return A new Booking instance
         */
        public Booking build() {
            return new Booking(member, startTime, endTime, day, month, year);
        }
    }

    /**
     * Validates the state of the Booking object
     */
    private void checkBooking() {
        Preconditions.checkState(member != null, "Member should not be null.");
        Preconditions.checkState(startTime != null, "Start time should not be null.");
        Preconditions.checkState(startTime.length() == 5, "Start time should have 5 characters."); // HH:MM
        Preconditions.checkState(endTime != null, "End time should not be null.");
        Preconditions.checkState(endTime.length() == 5, "End time should have 5 characters."); // HH:MM
        Preconditions.checkState(day >= 1 && day <= 31, "Day should be between 1 and 31.");
        Preconditions.checkState(month >= 1 && month <= 12, "Month should be between 1 and 12.");
        Preconditions.checkState(year > 2024, "Year should be 2025 or later.");

        Preconditions.checkState(isValidTimeFormat(startTime), "Start time must be valid HH:MM format.");
        Preconditions.checkState(isValidTimeFormat(endTime), "End time must be valid HH:MM format.");
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public Member getMember() {
        checkBooking();
        return member;
    }

    /**
     * Validates the time format string
     *
     * @param time The time string to validate in "HH:MM" format
     * @return true if time is in valid 24-hour format, false otherwise
     */
    private boolean isValidTimeFormat(String time) {
        if (time == null || time.length() != 5) {
            return false;
        }

        try {
            int hour = Integer.parseInt(time.substring(0, 2));
            int minute = Integer.parseInt(time.substring(3, 5));

            return hour >= 0 && hour < 24 && minute >= 0 && minute < 60;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Parses and validates a date string
     *
     * @param dateString The date string in "DD/MM/YY" format
     * @return An array containing [day, month, year] as integers
     * @throws InvalidDateException if date format is invalid or values are out of range
     */
    public static int[] parseAndValidateDate(String dateString) throws InvalidDateException {
        if (dateString == null || dateString.length() != 8) {
            throw new InvalidDateException("Date must be in DD/MM/YY format");
        }

        if (dateString.charAt(2) != '/' || dateString.charAt(5) != '/') {
            throw new InvalidDateException("Date must be in DD/MM/YY format with slashes");
        }

        try {
            int day = Integer.parseInt(dateString.substring(0, 2));
            int month = Integer.parseInt(dateString.substring(3, 5));
            int year = Integer.parseInt(dateString.substring(6, 8)) + 2000;

            if (day < 1 || day > 31) {
                throw new InvalidDateException("Day must be between 1 and 31");
            }
            if (month < 1 || month > 12) {
                throw new InvalidDateException("Month must be between 1 and 12");
            }
            if (year <= 2024) {
                throw new InvalidDateException("Year must be 2025 or later");
            }

            return new int[]{day, month, year};
        } catch (NumberFormatException e) {
            throw new InvalidDateException("Invalid date format. Use DD/MM/YY");
        }
    }

    /**
     * Parses and validates a time range string
     *
     * @param timeString The time string in "HH:MM-HH:MM" format
     * @return An array containing [startTime, endTime] as strings
     * @throws InvalidTimeFormatException if time format is invalid
     */
    public static String[] parseAndValidateTime(String timeString) throws InvalidTimeFormatException {
        if (timeString == null || timeString.length() != 11) {
            throw new InvalidTimeFormatException("Time must be in HH:MM-HH:MM format");
        }

        if (timeString.charAt(2) != ':' || timeString.charAt(5) != '-' || timeString.charAt(8) != ':') {
            throw new InvalidTimeFormatException("Time must be in HH:MM-HH:MM format");
        }

        String startTime = timeString.substring(0, 5);
        String endTime = timeString.substring(6, 11);

        return new String[]{startTime, endTime};
    }
}