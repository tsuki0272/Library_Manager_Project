package ca.umanitoba.cs.longkuma.domain.member;

import ca.umanitoba.cs.longkuma.domain.exceptions.*;
import ca.umanitoba.cs.longkuma.domain.media.MediaCopy;
import ca.umanitoba.cs.longkuma.domain.resource.Booking;
import ca.umanitoba.cs.longkuma.domain.resource.Resource;
import ca.umanitoba.cs.longkuma.logic.resource.BookingLogic;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private final String name;
    private final String password;
    private final List<MediaCopy> borrowedMedia;
    private final List<Resource> bookedResources;
    private final List<Constraint> constraints;

    private Member(String name, String password) {
        this.name = name;
        this.password = password;

        this.borrowedMedia = new ArrayList<>();
        this.bookedResources = new ArrayList<>();
        this.constraints = new ArrayList<>();

        checkMember();
    }

    // ===== BUILDER =====
    public static class MemberBuilder {
        private String name;
        private String password;

        public MemberBuilder name(String name) {
            Preconditions.checkArgument(name != null && !name.isEmpty());
            this.name = name;
            return this;
        }

        public MemberBuilder password(String password) {
            Preconditions.checkArgument(password != null && !password.isEmpty());
            this.password = password;
            return this;
        }

        public Member build() {
            return new Member(name, password);
        }
    }

    // ========= DOMAIN CHECKS =========
    private void checkMember() {
        Preconditions.checkState(name != null && !name.isEmpty());
        Preconditions.checkState(borrowedMedia != null);
        Preconditions.checkState(bookedResources != null);
        Preconditions.checkState(constraints != null);
    }

    // ========= DOMAIN GETTERS =========
    public String getName() { return name; }
    public String getPassword() { return password; }
    public List<MediaCopy> getBorrowedMedia() { return borrowedMedia; }
    public List<Resource> getBookedResources() { return bookedResources; }
    public List<Constraint> getConstraints() { return constraints; }
    public boolean hasConstraints() { return !constraints.isEmpty(); }


    public void addBorrowedCopy(MediaCopy copy) {
        borrowedMedia.add(copy);
    }

    public void removeBorrowedCopy(MediaCopy copy) {
        borrowedMedia.remove(copy);
    }

    public void addConstraint(Constraint c) {
        constraints.add(c);
    }

    public void addBookedResource(Resource r) {
        bookedResources.add(r);
    }

    // ========= Booking stays here (still domain-valid) =========
    public boolean bookResource(Resource resource, String dateString, String timeString)
            throws InvalidDateException, InvalidTimeFormatException, InvalidBookingFormatException,
            InvalidBookingDurationException, BookingLimitExceededException, TimeSlotUnavailableException, InvalidMemberException {

        Preconditions.checkNotNull(resource);
        Preconditions.checkNotNull(dateString);
        Preconditions.checkNotNull(timeString);

        // parse date/time using Booking logic
        int[] dateParts = BookingLogic.parseAndValidateDate(dateString);
        String[] timeParts = BookingLogic.parseAndValidateTime(timeString);

        Booking booking = new Booking.BookingBuilder()
                .member(this)
                .startTime(timeParts[0])
                .endTime(timeParts[1])
                .day(dateParts[0])
                .month(dateParts[1])
                .year(dateParts[2])
                .build();

        resource.addBooking(booking);
        bookedResources.add(resource);
        return true;
    }
}
