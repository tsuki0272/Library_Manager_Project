package ca.umanitoba.cs.longkuma.domain_model.resource;

import ca.umanitoba.cs.longkuma.domain_model.member.Member;
import com.google.common.base.Preconditions;

public class Booking {
    private Member member;

    private String startTime;
    private int startDay;
    private int startMonth;
    private int startYear;

    private String endTime;
    private int endDay;
    private int endMonth;
    private int endYear;

    public Booking(Member member, String startTime, int startDay, int startMonth, int startYear,
                   String endTime, int endDay, int endMonth, int endYear) {
        this.member = member;
        this.startTime = startTime;
        this.startDay = startDay;
        this.startMonth = startMonth;
        this.startYear = startYear;
        this.endTime = endTime;
        this.endDay = endDay;
        this.endMonth = endMonth;
        this.endYear = endYear;
        checkBooking();
    }

    private void checkBooking() {
        Preconditions.checkState(member != null, "Member should not be null.");
        Preconditions.checkState(startTime != null, "Start time should not be null.");
        Preconditions.checkState(startTime.length() >= 1, "Start time should have at least one symbol.");
        Preconditions.checkState(endTime != null, "End time should not be null.");
        Preconditions.checkState(endTime.length() >= 1, "End time should have at least one symbol.");
        Preconditions.checkState(startDay >= 1 && startDay <= 31, "Start day should be between 1 and 31.");
        Preconditions.checkState(startMonth >= 1 && startMonth <= 12, "Start month should be between 1 and 12.");
        Preconditions.checkState(startYear >= 2024, "Start year should be 2025 or later.");
        Preconditions.checkState(endDay >= 1 && endDay <= 31, "End day should be between 1 and 31.");
        Preconditions.checkState(endMonth >= 1 && endMonth <= 12, "End month should be between 1 and 12.");
        Preconditions.checkState(endYear >= 2024, "End year should be 2025 or later.");
    }

    public Member getMember() {
        checkBooking();
        return member;
    }
}
