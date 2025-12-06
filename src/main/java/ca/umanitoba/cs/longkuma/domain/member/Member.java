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

    /*
     * Private constructor for Member
     * Initializes member with name and password, creates empty lists for borrowed media, booked resources, and constraints
     * Validates the member state after construction
     *
     * @param name The name of the member
     * @param password The password for the member account
     */
    private Member(String name, String password) {
        this.name = name;
        this.password = password;

        this.borrowedMedia = new ArrayList<>();
        this.bookedResources = new ArrayList<>();
        this.constraints = new ArrayList<>();

        checkMember();
    }

    public static class MemberBuilder {
        private String name;
        private String password;

        /*
         * Sets the name for the member being built
         * Validates that name is non-null and non-empty
         *
         * @param name The name of the member
         * @return MemberBuilder instance for method chaining
         */
        public MemberBuilder name(String name) {
            Preconditions.checkArgument(name != null && !name.isEmpty());
            this.name = name;
            return this;
        }

        /*
         * Sets the password for the member being built
         * Validates that password is non-null and non-empty
         *
         * @param password The password for the member account
         * @return MemberBuilder instance for method chaining
         */
        public MemberBuilder password(String password) {
            Preconditions.checkArgument(password != null && !password.isEmpty());
            this.password = password;
            return this;
        }

        /*
         * Builds and returns a new Member instance with configured parameters
         *
         * @return A new Member object
         */
        public Member build() {
            return new Member(name, password);
        }
    }

    /*
     * Validates the internal state of the Member object
     * Ensures all required fields are non-null and meet minimum requirements
     */
    private void checkMember() {
        Preconditions.checkState(name != null && !name.isEmpty());
        Preconditions.checkState(borrowedMedia != null);
        Preconditions.checkState(bookedResources != null);
        Preconditions.checkState(constraints != null);
    }

    // Getters:
    public String getName() { return name; }
    public String getPassword() { return password; }
    public List<MediaCopy> getBorrowedMedia() { return borrowedMedia; }
    public List<Resource> getBookedResources() { return bookedResources; }
    public List<Constraint> getConstraints() { return constraints; }
    public boolean hasConstraints() { return !constraints.isEmpty(); }

    /*
     * Adds a borrowed media copy to the member's collection
     *
     * @param copy The media copy to add
     */
    public void addBorrowedCopy(MediaCopy copy) {
        borrowedMedia.add(copy);
    }

    /*
     * Removes a borrowed media copy from the member's collection
     *
     * @param copy The media copy to remove
     */
    public void removeBorrowedCopy(MediaCopy copy) {
        borrowedMedia.remove(copy);
    }

    /*
     * Adds a constraint to the member's collection
     *
     * @param c The constraint to add
     */
    public void addConstraint(Constraint c) {
        constraints.add(c);
    }

    /*
     * Adds a booked resource to the member's collection
     *
     * @param r The resource to add
     */
    public void addBookedResource(Resource r) {
        bookedResources.add(r);
    }

    /*
     * Books a resource for the member at a specified date and time
     * Parses and validates the date and time, creates a booking, and adds it to the resource
     * Validates all parameters before processing
     *
     * @param resource The resource to book
     * @param dateString The date string in DD/MM/YY format
     * @param timeString The time string in HH:MM-HH:MM format
     * @return true if the booking was successful
     * @throws InvalidDateException if the date format is invalid
     * @throws InvalidTimeFormatException if the time format is invalid
     * @throws InvalidBookingFormatException if the booking format is invalid
     * @throws InvalidBookingDurationException if the booking duration is invalid
     * @throws BookingLimitExceededException if the member has exceeded booking limits
     * @throws TimeSlotUnavailableException if the requested time slot is unavailable
     * @throws InvalidMemberException if the member is invalid
     */
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