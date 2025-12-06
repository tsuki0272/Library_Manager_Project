package ca.umanitoba.cs.longkuma.domain.resource;

import ca.umanitoba.cs.longkuma.domain.exceptions.InvalidDateException;
import ca.umanitoba.cs.longkuma.domain.exceptions.InvalidMemberException;
import ca.umanitoba.cs.longkuma.domain.exceptions.InvalidTimeFormatException;
import ca.umanitoba.cs.longkuma.domain.member.Member;
import com.google.common.base.Preconditions;

public class Booking {

    private final Member member;
    private final String startTime;
    private final String endTime;
    private final int day;
    private final int month;
    private final int year;

    /*
     * Private constructor for Booking
     * Initializes booking with member, time range, and date information
     * Validates the booking state after construction
     *
     * @param member The member making the booking
     * @param startTime The start time of the booking in HH:MM format
     * @param endTime The end time of the booking in HH:MM format
     * @param day The day of the booking (1-31)
     * @param month The month of the booking (1-12)
     * @param year The year of the booking
     */
    private Booking(Member member, String startTime, String endTime,
                    int day, int month, int year) {
        this.member = member;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.month = month;
        this.year = year;
        checkBooking();
    }

    public static class BookingBuilder {
        private Member member;
        private String startTime;
        private String endTime;
        private int day;
        private int month;
        private int year;

        /*
         * Sets the member for the booking being built
         *
         * @param member The member making the booking
         * @return BookingBuilder instance for method chaining
         * @throws InvalidMemberException if member is null
         */
        public BookingBuilder member(Member member) throws InvalidMemberException {
            if (member == null) throw new InvalidMemberException("Member cannot be null");
            this.member = member;
            return this;
        }

        /*
         * Sets the start time for the booking being built
         *
         * @param startTime The start time in HH:MM format
         * @return BookingBuilder instance for method chaining
         * @throws InvalidTimeFormatException if startTime is null or not in HH:MM format
         */
        public BookingBuilder startTime(String startTime) throws InvalidTimeFormatException {
            if (startTime == null || startTime.length() != 5)
                throw new InvalidTimeFormatException("Start time must be HH:MM");
            this.startTime = startTime;
            return this;
        }

        /*
         * Sets the end time for the booking being built
         *
         * @param endTime The end time in HH:MM format
         * @return BookingBuilder instance for method chaining
         * @throws InvalidTimeFormatException if endTime is null or not in HH:MM format
         */
        public BookingBuilder endTime(String endTime) throws InvalidTimeFormatException {
            if (endTime == null || endTime.length() != 5)
                throw new InvalidTimeFormatException("End time must be HH:MM");
            this.endTime = endTime;
            return this;
        }

        /*
         * Sets the day for the booking being built
         *
         * @param day The day of the booking (1-31)
         * @return BookingBuilder instance for method chaining
         * @throws InvalidDateException if day is not between 1 and 31
         */
        public BookingBuilder day(int day) throws InvalidDateException {
            if (day < 1 || day > 31)
                throw new InvalidDateException("Day must be 1–31");
            this.day = day;
            return this;
        }

        /*
         * Sets the month for the booking being built
         *
         * @param month The month of the booking (1-12)
         * @return BookingBuilder instance for method chaining
         * @throws InvalidDateException if month is not between 1 and 12
         */
        public BookingBuilder month(int month) throws InvalidDateException {
            if (month < 1 || month > 12)
                throw new InvalidDateException("Month must be 1–12");
            this.month = month;
            return this;
        }

        /*
         * Sets the year for the booking being built
         *
         * @param year The year of the booking
         * @return BookingBuilder instance for method chaining
         * @throws InvalidDateException if year is 2024 or earlier
         */
        public BookingBuilder year(int year) throws InvalidDateException {
            if (year <= 2024)
                throw new InvalidDateException("Year must be 2025 or later");
            this.year = year;
            return this;
        }

        /*
         * Builds and returns a new Booking instance with configured parameters
         *
         * @return A new Booking object
         */
        public Booking build() {
            return new Booking(member, startTime, endTime, day, month, year);
        }
    }

    /*
     * Validates the internal state of the Booking object
     * Ensures all required fields are non-null and meet minimum requirements
     * Validates time format for both start and end times
     */
    private void checkBooking() {
        Preconditions.checkState(member != null, "Member cannot be null.");
        Preconditions.checkState(startTime != null && startTime.length() == 5, "Start time invalid.");
        Preconditions.checkState(endTime != null && endTime.length() == 5, "End time invalid.");
        Preconditions.checkState(day >= 1 && day <= 31, "Invalid day.");
        Preconditions.checkState(month >= 1 && month <= 12, "Invalid month.");
        Preconditions.checkState(year > 2024, "Invalid year.");
        Preconditions.checkState(validTime(startTime), "Invalid time format for start.");
        Preconditions.checkState(validTime(endTime), "Invalid time format for end.");
    }

    /*
     * Validates that a time string is in correct format and within valid ranges
     * Checks that hours are 0-23 and minutes are 0-59
     *
     * @param time The time string to validate in HH:MM format
     * @return true if time is valid, false otherwise
     */
    private boolean validTime(String time) {
        try {
            int hour = Integer.parseInt(time.substring(0, 2));
            int minute = Integer.parseInt(time.substring(3, 5));
            return hour >= 0 && hour < 24 && minute >= 0 && minute < 60;
        } catch (Exception e) {
            return false;
        }
    }

    // Getters:
    public Member getMember() { return member; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
}