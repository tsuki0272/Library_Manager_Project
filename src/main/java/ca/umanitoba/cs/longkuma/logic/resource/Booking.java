package ca.umanitoba.cs.longkuma.logic.resource;

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

        public BookingBuilder member(Member member) throws Exception {
            if (member == null) {
                throw new Exception("Member should not be null.");
            }
            this.member = member;
            return this;
        }

        public BookingBuilder startTime(String startTime) throws Exception {
            if (startTime == null) {
                throw new Exception("Start time should not be null.");
            }
            if (startTime.length() != 5) {
                throw new Exception("Start time should have 5 characters (HH:MM).");
            }
            // CHANGED: Removed validation for specific minute values
            // Now accepts any valid HH:MM format (will be validated against Resource's timeslots)
            this.startTime = startTime;
            return this;
        }

        public BookingBuilder endTime(String endTime) throws Exception {
            if (endTime == null) {
                throw new Exception("End time should not be null.");
            }
            if (endTime.length() != 5) {
                throw new Exception("End time should have 5 characters (HH:MM).");
            }
            // CHANGED: Removed validation for specific minute values
            // Now accepts any valid HH:MM format (will be validated against Resource's timeslots)
            this.endTime = endTime;
            return this;
        }

        public BookingBuilder day(int day) throws Exception {
            if (day < 1 || day > 31) {
                throw new Exception("Day should be between 1 and 31.");
            }
            this.day = day;
            return this;
        }

        public BookingBuilder month(int month) throws Exception {
            if (month < 1 || month > 12) {
                throw new Exception("Month should be between 1 and 12.");
            }
            this.month = month;
            return this;
        }

        public BookingBuilder year(int year) throws Exception {
            if (year <= 2024) {
                throw new Exception("Year should be 2025 or later.");
            }
            this.year = year;
            return this;
        }

        public Booking build() {
            return new Booking(member, startTime, endTime, day, month, year);
        }
    }

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

    // NEW: Helper method to validate time format
    private boolean isValidTimeFormat(String time) {
        if (time == null || time.length() != 5) {
            return false;
        }

        try {
            int hour = Integer.parseInt(time.substring(0, 2));
            int minute = Integer.parseInt(time.substring(3, 5));

            // Check if hour is 0-23 and minute is 0-59
            return hour >= 0 && hour < 24 && minute >= 0 && minute < 60;
        } catch (NumberFormatException e) {
            return false;
        }
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
}