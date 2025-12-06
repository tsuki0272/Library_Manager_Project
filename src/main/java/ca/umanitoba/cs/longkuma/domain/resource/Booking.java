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

    private Booking(Member member, String startTime, String endTime,
            int day, int month, int year) {

        this.member = member;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.month = month;
        this.year = year;

        validateBooking();
    }

    public static class BookingBuilder {
        private Member member;
        private String startTime;
        private String endTime;
        private int day;
        private int month;
        private int year;

        public BookingBuilder member(Member member) throws InvalidMemberException {
            if (member == null) throw new InvalidMemberException("Member cannot be null");
            this.member = member;
            return this;
        }

        public BookingBuilder startTime(String startTime) throws InvalidTimeFormatException {
            if (startTime == null || startTime.length() != 5)
                throw new InvalidTimeFormatException("Start time must be HH:MM");
            this.startTime = startTime;
            return this;
        }

        public BookingBuilder endTime(String endTime) throws InvalidTimeFormatException {
            if (endTime == null || endTime.length() != 5)
                throw new InvalidTimeFormatException("End time must be HH:MM");
            this.endTime = endTime;
            return this;
        }

        public BookingBuilder day(int day) throws InvalidDateException {
            if (day < 1 || day > 31)
                throw new InvalidDateException("Day must be 1–31");
            this.day = day;
            return this;
        }

        public BookingBuilder month(int month) throws InvalidDateException {
            if (month < 1 || month > 12)
                throw new InvalidDateException("Month must be 1–12");
            this.month = month;
            return this;
        }

        public BookingBuilder year(int year) throws InvalidDateException {
            if (year <= 2024)
                throw new InvalidDateException("Year must be 2025 or later");
            this.year = year;
            return this;
        }

        public Booking build() {
            return new Booking(member, startTime, endTime, day, month, year);
        }
    }

    private void validateBooking() {
        Preconditions.checkState(member != null, "Member cannot be null.");
        Preconditions.checkState(startTime != null && startTime.length() == 5, "Start time invalid.");
        Preconditions.checkState(endTime != null && endTime.length() == 5, "End time invalid.");
        Preconditions.checkState(day >= 1 && day <= 31, "Invalid day.");
        Preconditions.checkState(month >= 1 && month <= 12, "Invalid month.");
        Preconditions.checkState(year > 2024, "Invalid year.");
        Preconditions.checkState(validTime(startTime), "Invalid time format for start.");
        Preconditions.checkState(validTime(endTime), "Invalid time format for end.");
    }

    private boolean validTime(String time) {
        try {
            int hour = Integer.parseInt(time.substring(0, 2));
            int minute = Integer.parseInt(time.substring(3, 5));
            return hour >= 0 && hour < 24 && minute >= 0 && minute < 60;
        } catch (Exception e) {
            return false;
        }
    }

    public Member getMember() { return member; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }
}
