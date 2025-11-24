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

        public BookingBuilder member(Member member) throws InvalidMemberException {
            if (member == null) {
                throw new InvalidMemberException("Member should not be null.");
            }
            this.member = member;
            return this;
        }

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

        public BookingBuilder day(int day) throws InvalidDateException {
            if (day < 1 || day > 31) {
                throw new InvalidDateException("Day should be between 1 and 31.");
            }
            this.day = day;
            return this;
        }

        public BookingBuilder month(int month) throws InvalidDateException {
            if (month < 1 || month > 12) {
                throw new InvalidDateException("Month should be between 1 and 12.");
            }
            this.month = month;
            return this;
        }

        public BookingBuilder year(int year) throws InvalidDateException {
            if (year <= 2024) {
                throw new InvalidDateException("Year should be 2025 or later.");
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