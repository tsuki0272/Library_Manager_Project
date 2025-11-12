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

    public Booking(Member member, String startTime, String endTime, int day, int month, int year) {
        this.member = member;
        this.startTime = startTime;
        this.day = day;
        this.month = month;
        this.year = year;
        this.endTime = endTime;
        checkBooking();
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
