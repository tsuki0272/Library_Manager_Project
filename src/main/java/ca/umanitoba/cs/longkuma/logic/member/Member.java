package ca.umanitoba.cs.longkuma.logic.member;

import ca.umanitoba.cs.longkuma.logic.exceptions.*;
import ca.umanitoba.cs.longkuma.logic.media.Media;
import ca.umanitoba.cs.longkuma.logic.media.MediaCopy;
import ca.umanitoba.cs.longkuma.logic.resource.Booking;
import ca.umanitoba.cs.longkuma.logic.resource.Resource;
import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class Member {
    final private ArrayList<MediaCopy> borrowedMedia;
    final private ArrayList<Resource> resources;
    final private ArrayList<Constraint> constraints;
    final private String name;
    final private String password;

    private Member(String name, String password) {
        this.name = name;
        this.password = password;
        this.borrowedMedia = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.constraints = new ArrayList<>();
        checkMember();
    }

    public static class MemberBuilder {
        private String name;
        private String password;
        public MemberBuilder() {}

        /**
         * Sets the name for the member being built
         *
         * @param name The name of the member
         * @return The MemberBuilder instance for method chaining
         * @throws InvalidMemberException if name is null or empty
         */
        public MemberBuilder name(String name) throws InvalidMemberException {
            if(name == null || name.isEmpty()) {
                throw new InvalidMemberException("Name should not be null or empty.");
            }
            this.name = name;
            return this;
        }

        /**
         * Sets the password for the member being built
         *
         * @param password The password for the member
         * @return The MemberBuilder instance for method chaining
         * @throws InvalidMemberException if password is null or empty
         */
        public MemberBuilder password(String password) throws InvalidMemberException {
            if(password == null || password.isEmpty()) {
                throw new InvalidMemberException("Name should not be null or empty.");
            }
            this.password = password;
            return this;
        }

        /**
         * Builds and returns a new Member instance with the configured properties
         *
         * @return A new Member instance
         */
        public Member build() {
            return new Member(name, password);
        }
    }

    /**
     * Validates the state of the Member object and its components
     */
    private void checkMember() {
        Preconditions.checkState(name != null, "Member name should not be null.");
        Preconditions.checkState(name.length() >= 1, "Member name should have at least one symbol.");
        Preconditions.checkState(borrowedMedia != null, "Borrowed media list should not be null.");
        Preconditions.checkState(resources != null, "Resources list should not be null.");
        Preconditions.checkState(constraints != null, "Constraints list should not be null.");

        for (MediaCopy copy : borrowedMedia) {
            Preconditions.checkState(copy != null, "Individual media copies should never be null.");
        }

        for (Resource resource : resources) {
            Preconditions.checkState(resource != null, "Individual resources should never be null.");
        }

        for (Constraint constraint : constraints) {
            Preconditions.checkState(constraint != null, "Individual constraints should never be null.");
        }
    }

    public String getName() {
        return name;
    }

    public String getPassword() {return password;}

    public ArrayList<MediaCopy> getBorrowedMedia() {
        return borrowedMedia;
    }

    /**
     * Borrows a media item for this member
     *
     * @param media The media to borrow
     * @return true if media was successfully borrowed, false otherwise
     */
    public boolean borrowMedia(Media media) {
        checkMember();
        Preconditions.checkNotNull(media, "Media cannot be null");
        boolean borrowed = false;

        if (!hasConstraints()) {
            MediaCopy borrowedCopy = media.borrowCopy(this);
            if (borrowedCopy != null) {
                borrowedMedia.add(borrowedCopy);
                borrowed = true;
            } else {
                // No copy available - add to waitlist automatically
                media.addToWaitlist(this);
            }
        }

        return borrowed;
    }

    /**
     * Returns a borrowed media copy
     *
     * @param copy The media copy to return
     * @return true if media was successfully returned, false otherwise
     */
    public boolean returnMedia(MediaCopy copy) {
        checkMember();
        Preconditions.checkNotNull(copy, "Media copy cannot be null");

        boolean removed = borrowedMedia.remove(copy);

        if (removed) {
            // Return the copy (makes it available again)
            copy.returnCopy();
            checkMember();
        }

        return removed;
    }

    /**
     * Books a resource for this member with specified date and time
     *
     * @param resource The resource to book
     * @param dateString The date string in valid format
     * @param timeString The time string in valid format
     * @return true if resource was successfully booked, false otherwise
     * @throws InvalidDateException if date format is invalid
     * @throws InvalidTimeFormatException if time format is invalid
     * @throws InvalidBookingFormatException if booking format is invalid
     * @throws InvalidBookingDurationException if booking duration is invalid
     * @throws BookingLimitExceededException if booking limit is exceeded
     * @throws TimeSlotUnavailableException if time slot is not available
     * @throws InvalidMemberException if member is invalid
     */
    public boolean bookResource(Resource resource, String dateString, String timeString)
            throws InvalidDateException, InvalidTimeFormatException, InvalidBookingFormatException,
            InvalidBookingDurationException, BookingLimitExceededException, TimeSlotUnavailableException,
            InvalidMemberException {
        checkMember();
        Preconditions.checkNotNull(resource, "Resource cannot be null");
        Preconditions.checkNotNull(dateString, "Date string cannot be null");
        Preconditions.checkNotNull(timeString, "Time string cannot be null");

        // Parse and validate date
        int[] dateParts = Booking.parseAndValidateDate(dateString);
        int day = dateParts[0];
        int month = dateParts[1];
        int year = dateParts[2];

        // Parse and validate time
        String[] timeParts = Booking.parseAndValidateTime(timeString);
        String startTime = timeParts[0];
        String endTime = timeParts[1];

        // Create booking
        Booking booking = new Booking.BookingBuilder()
                .member(this)
                .startTime(startTime)
                .endTime(endTime)
                .day(day)
                .month(month)
                .year(year)
                .build();

        // Add booking to resource (will throw exceptions if invalid)
        resource.addBooking(booking);

        // Add resource to member's list
        boolean added = resources.add(resource);
        checkMember();
        return added;
    }

    /**
     * Adds a constraint to this member
     *
     * @param constraint The constraint to add
     * @return true if constraint was successfully added, false otherwise
     */
    public boolean addConstraint(Constraint constraint) {
        checkMember();
        Preconditions.checkNotNull(constraint, "Constraint cannot be null");
        boolean added = constraints.add(constraint);
        checkMember();
        return added;
    }

    /**
     * Checks if this member has any constraints
     *
     * @return true if member has at least one constraint, false otherwise
     */
    public boolean hasConstraints() {
        return constraints.size() > 0;
    }
}