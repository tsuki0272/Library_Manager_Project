package ca.umanitoba.cs.longkuma.logic.resource;

import ca.umanitoba.cs.longkuma.logic.exceptions.*;
import ca.umanitoba.cs.longkuma.logic.member.Member;
import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class Resource {
    final private ArrayList<Booking> bookings;
    final String resourceName;
    final private String openingTime; // Format: "HH:MM"
    final private String closingTime; // Format: "HH:MM"
    final private int timeslotLength; // in minutes
    final private ArrayList<int[]> coordinates;

    private Resource(String resourceName, String openingTime, String closingTime, int timeslotLength, ArrayList<int[]> coordinates) {
        this.bookings = new ArrayList<>();
        this.resourceName = resourceName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.timeslotLength = timeslotLength;
        this.coordinates = coordinates;
        checkResource();
    }

    public static class ResourceBuilder {
        private String resourceName;
        private String openingTime;
        private String closingTime;
        private int timeslotLength;
        private ArrayList<int[]> coordinates;

        public ResourceBuilder() {}

        /**
         * Sets the resource name for the resource being built
         *
         * @param resourceName The name of the resource
         * @return The ResourceBuilder instance for method chaining
         * @throws InvalidResourceException if resourceName is null or empty
         */
        public ResourceBuilder resourceName(String resourceName) throws InvalidResourceException {
            if (resourceName == null || resourceName.isEmpty()) {
                throw new InvalidResourceException("Resource name should not be null or empty.");
            }
            this.resourceName = resourceName;
            return this;
        }

        /**
         * Sets the opening time for the resource being built
         *
         * @param openingTime The opening time in "HH:MM" format
         * @return The ResourceBuilder instance for method chaining
         * @throws InvalidResourceException if openingTime is null or not 5 characters
         */
        public ResourceBuilder openingTime(String openingTime) throws InvalidResourceException {
            if (openingTime == null || openingTime.length() != 5) {
                throw new InvalidResourceException("Opening time should be in HH:MM format.");
            }
            this.openingTime = openingTime;
            return this;
        }

        /**
         * Sets the closing time for the resource being built
         *
         * @param closingTime The closing time in "HH:MM" format
         * @return The ResourceBuilder instance for method chaining
         * @throws InvalidResourceException if closingTime is null or not 5 characters
         */
        public ResourceBuilder closingTime(String closingTime) throws InvalidResourceException {
            if (closingTime == null || closingTime.length() != 5) {
                throw new InvalidResourceException("Closing time should be in HH:MM format.");
            }
            this.closingTime = closingTime;
            return this;
        }

        /**
         * Sets the timeslot length for the resource being built
         *
         * @param timeslotLength The length of each timeslot in minutes
         * @return The ResourceBuilder instance for method chaining
         * @throws InvalidResourceException if timeslotLength is not between 1 and 1440 minutes
         */
        public ResourceBuilder timeslotLength(int timeslotLength) throws InvalidResourceException {
            if (timeslotLength <= 0 || timeslotLength > 1440) {
                throw new InvalidResourceException("Timeslot length should be between 1 and 1440 minutes.");
            }
            this.timeslotLength = timeslotLength;
            return this;
        }

        /**
         * Sets the coordinates for the resource being built
         *
         * @param coordinates The list of coordinate pairs where the resource is located
         * @return The ResourceBuilder instance for method chaining
         * @throws InvalidResourceException if coordinates is null or contains invalid coordinate pairs
         */
        public ResourceBuilder coordinates(ArrayList<int[]> coordinates) throws InvalidResourceException {
            if (coordinates == null) {
                throw new InvalidResourceException("Coordinates should not be null.");
            }
            for(int[] coordinate : coordinates) {
                if (coordinate == null) {
                    throw new InvalidResourceException("Individual coordinates should not be null.");
                }
                if (coordinate.length != 2) {
                    throw new InvalidResourceException("Each coordinate should have exactly 2 values (row, column).");
                }
            }
            this.coordinates = coordinates;
            return this;
        }

        /**
         * Builds and returns a new Resource instance with the configured properties
         *
         * @return A new Resource instance
         */
        public Resource build() {
            return new Resource(resourceName, openingTime, closingTime, timeslotLength, coordinates);
        }
    }

    /**
     * Validates the state of the Resource object and its components
     */
    private void checkResource() {
        Preconditions.checkState(bookings != null, "Bookings list should not be null.");
        Preconditions.checkState(resourceName != null, "Resource name should not be null.");
        Preconditions.checkState(resourceName.length() >= 1, "Resource name should have at least one symbol.");
        Preconditions.checkState(openingTime != null, "Opening time should not be null.");
        Preconditions.checkState(openingTime.length() == 5, "Opening time should be in HH:MM format.");
        Preconditions.checkState(closingTime != null, "Closing time should not be null.");
        Preconditions.checkState(closingTime.length() == 5, "Closing time should be in HH:MM format.");
        Preconditions.checkState(timeslotLength > 0, "Timeslot length should be positive.");
        Preconditions.checkState(coordinates != null, "Coordinates should not be null.");
        Preconditions.checkState(coordinates.size() >= 1, "Coordinates should have at least one coordinate.");

        for (Booking booking : bookings) {
            Preconditions.checkState(booking != null, "Individual bookings should never be null.");
        }

        for (int[] coordinate : coordinates) {
            Preconditions.checkState(coordinate != null, "Individual coordinates should never be null.");
            Preconditions.checkState(coordinate.length == 2, "Each coordinate should have exactly 2 values.");
        }
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public int getTimeslotLength() {
        return timeslotLength;
    }

    public ArrayList<Booking> getBookings() {
        checkResource();
        return bookings;
    }

    /**
     * Gets the booking time for a specific member on a specific date
     *
     * @param member The member to search bookings for
     * @param bookingDate The date in "DD/MM/YY" format to search for
     * @return The booking time as "HH:MM - HH:MM" string, or null if no booking found
     */
    public String getBooking(Member member, String bookingDate) {
        checkResource();
        Preconditions.checkNotNull(member, "Member cannot be null");
        Preconditions.checkNotNull(bookingDate, "Booking date cannot be null");

        Booking foundBooking = null;
        String bookingTime = null;
        int index = 0;

        // Parse the booking date
        int day = Integer.parseInt(bookingDate.substring(0, 2));
        int month = Integer.parseInt(bookingDate.substring(3, 5));
        int year = Integer.parseInt(bookingDate.substring(6, 8)) + 2000;

        while (foundBooking == null && index < bookings.size()) {
            Booking currentBooking = bookings.get(index);
            boolean sameMember = currentBooking.getMember().equals(member);
            boolean sameDate = currentBooking.getDay() == day &&
                    currentBooking.getMonth() == month &&
                    currentBooking.getYear() == year;
            if (sameMember && sameDate) {
                foundBooking = currentBooking;
                bookingTime = foundBooking.getStartTime() + " - " + foundBooking.getEndTime();
            }
            index++;
        }
        checkResource();
        return bookingTime;
    }

    /**
     * Adds a booking to the resource after validating all constraints
     *
     * @param booking The booking to add
     * @return true if booking was successfully added, false otherwise
     * @throws InvalidBookingFormatException if booking format doesn't align with timeslots or operating hours
     * @throws InvalidBookingDurationException if booking duration exceeds 2 hours
     * @throws BookingLimitExceededException if member already has a booking for the same day
     * @throws TimeSlotUnavailableException if the time slot is already booked
     */
    public boolean addBooking(Booking booking) throws InvalidBookingFormatException,
            InvalidBookingDurationException,
            BookingLimitExceededException,
            TimeSlotUnavailableException {
        checkResource();
        Preconditions.checkNotNull(booking, "Booking cannot be null");

        if (!validBookingFormat(booking)) {
            throw new InvalidBookingFormatException("Booking must align with " + timeslotLength +
                    "-minute timeslots and be within operating hours (" + openingTime + " - " + closingTime + ")");
        }
        if (!validBookingTime(booking)) {
            throw new InvalidBookingDurationException("Booking must be 2 hours or less");
        }
        if (!validBookingLimit(booking)) {
            throw new BookingLimitExceededException("You have already booked a resource for this day");
        }
        if (!availableTimeSlot(booking)) {
            throw new TimeSlotUnavailableException("This time slot has already been booked");
        }

        boolean added = bookings.add(booking);
        checkResource();
        return added;
    }

    /**
     * Deletes a booking from the resource
     *
     * @param booking The booking to delete
     * @return true if booking was successfully removed, false otherwise
     */
    public boolean deleteBooking(Booking booking) {
        checkResource();
        Preconditions.checkNotNull(booking, "Booking cannot be null");

        boolean removed = bookings.remove(booking);

        checkResource();
        return removed;
    }

    /**
     * Checks if a time aligns with the resource's timeslot boundaries
     *
     * @param time The time string to check in "HH:MM" format
     * @return true if time aligns with timeslot boundaries, false otherwise
     */
    private boolean isValidTimeslotBoundary(String time) {
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3, 5));

        int openHour = Integer.parseInt(openingTime.substring(0, 2));
        int openMinute = Integer.parseInt(openingTime.substring(3, 5));

        int timeInMinutes = hour * 60 + minute;
        int openingInMinutes = openHour * 60 + openMinute;

        int minutesSinceOpening = timeInMinutes - openingInMinutes;

        return minutesSinceOpening >= 0 && minutesSinceOpening % timeslotLength == 0;
    }

    /**
     * Validates the booking format against timeslot boundaries and operating hours
     *
     * @param booking The booking to validate
     * @return true if booking format is valid, false otherwise
     */
    public boolean validBookingFormat(Booking booking) {
        boolean isValid = true;
        String startTime = booking.getStartTime();
        String endTime = booking.getEndTime();

        // Check time format (HH:MM)
        if (startTime.length() != 5 || endTime.length() != 5) {
            isValid = false;
        } else {
            // Parse hours and minutes
            int startHour = Integer.parseInt(startTime.substring(0, 2));
            int startMinute = Integer.parseInt(startTime.substring(3, 5));
            int endHour = Integer.parseInt(endTime.substring(0, 2));
            int endMinute = Integer.parseInt(endTime.substring(3, 5));

            int openHour = Integer.parseInt(openingTime.substring(0, 2));
            int openMinute = Integer.parseInt(openingTime.substring(3, 5));
            int closeHour = Integer.parseInt(closingTime.substring(0, 2));
            int closeMinute = Integer.parseInt(closingTime.substring(3, 5));

            int startInMinutes = startHour * 60 + startMinute;
            int endInMinutes = endHour * 60 + endMinute;
            int openInMinutes = openHour * 60 + openMinute;
            int closeInMinutes = closeHour * 60 + closeMinute;

            if (!isValidTimeslotBoundary(startTime) || !isValidTimeslotBoundary(endTime)) {
                isValid = false;
            }

            if (startInMinutes < openInMinutes || endInMinutes > closeInMinutes) {
                isValid = false;
            }

            if (startInMinutes >= endInMinutes) {
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Validates the booking duration does not exceed 2 hours
     *
     * @param booking The booking to validate
     * @return true if booking duration is 2 hours or less, false otherwise
     */
    public boolean validBookingTime(Booking booking) {
        String startTime = booking.getStartTime();
        String endTime = booking.getEndTime();
        int startHour = Integer.parseInt(startTime.substring(0, 2));
        int startMinute = Integer.parseInt(startTime.substring(3, 5));
        int endHour = Integer.parseInt(endTime.substring(0, 2));
        int endMinute = Integer.parseInt(endTime.substring(3, 5));

        int startInMinutes = startHour * 60 + startMinute;
        int endInMinutes = endHour * 60 + endMinute;

        int durationInMinutes = endInMinutes - startInMinutes;

        // Check if duration is 2 hours (120 minutes) or less
        return durationInMinutes <= 120;
    }

    /**
     * Validates that a member doesn't exceed the daily booking limit
     *
     * @param newBooking The new booking to validate
     * @return true if member doesn't have another booking on the same day, false otherwise
     */
    public boolean validBookingLimit(Booking newBooking) {
        boolean validBookingLimit = true;
        int index = 0;

        while (validBookingLimit && index < bookings.size()) {
            Booking existingBooking = bookings.get(index);

            // Check if same member and same date
            boolean sameMember = existingBooking.getMember().getName().equals(newBooking.getMember().getName());
            boolean sameDate = existingBooking.getDay() == newBooking.getDay() &&
                    existingBooking.getMonth() == newBooking.getMonth() &&
                    existingBooking.getYear() == newBooking.getYear();

            if (sameMember && sameDate) {
                validBookingLimit = false; // Member already has a booking this day
            }

            index++;
        }

        return validBookingLimit;
    }

    /**
     * Checks if the time slot for a booking is available
     *
     * @param newBooking The new booking to check availability for
     * @return true if time slot is available, false if already booked
     */
    public boolean availableTimeSlot(Booking newBooking) {
        boolean available = true;
        int index = 0;

        while (available && index < bookings.size()) {
            Booking booking = bookings.get(index);

            // Check if same date
            boolean sameDate = booking.getDay() == newBooking.getDay() &&
                    booking.getMonth() == newBooking.getMonth() &&
                    booking.getYear() == newBooking.getYear();

            if (sameDate) {
                // Parse times including minutes
                int newStartHour = Integer.parseInt(newBooking.getStartTime().substring(0, 2));
                int newStartMinute = Integer.parseInt(newBooking.getStartTime().substring(3, 5));
                int newEndHour = Integer.parseInt(newBooking.getEndTime().substring(0, 2));
                int newEndMinute = Integer.parseInt(newBooking.getEndTime().substring(3, 5));

                int existingStartHour = Integer.parseInt(booking.getStartTime().substring(0, 2));
                int existingStartMinute = Integer.parseInt(booking.getStartTime().substring(3, 5));
                int existingEndHour = Integer.parseInt(booking.getEndTime().substring(0, 2));
                int existingEndMinute = Integer.parseInt(booking.getEndTime().substring(3, 5));

                // Convert to minutes since midnight
                int newStart = newStartHour * 60 + newStartMinute;
                int newEnd = newEndHour * 60 + newEndMinute;
                int existingStart = existingStartHour * 60 + existingStartMinute;
                int existingEnd = existingEndHour * 60 + existingEndMinute;

                // Check for time overlap
                boolean timeOverlap = (newStart < existingEnd && newEnd > existingStart);

                if (timeOverlap) {
                    available = false;
                }
            }
            index++;
        }
        return available;
    }

    public ArrayList<int[]> getCoordinates() {
        return coordinates;
    }
}