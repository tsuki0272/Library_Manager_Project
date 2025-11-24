package ca.umanitoba.cs.longkuma.logic.resource;

import ca.umanitoba.cs.longkuma.logic.exceptions.BookingLimitExceededException;
import ca.umanitoba.cs.longkuma.logic.exceptions.InvalidBookingDurationException;
import ca.umanitoba.cs.longkuma.logic.exceptions.InvalidBookingFormatException;
import ca.umanitoba.cs.longkuma.logic.exceptions.TimeSlotUnavailableException;
import ca.umanitoba.cs.longkuma.logic.member.Member;
import com.google.common.base.Preconditions;

import java.lang.reflect.Array;
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

        public ResourceBuilder resourceName(String resourceName) throws Exception {
            if (resourceName == null || resourceName.isEmpty()) {
                throw new Exception("Resource name should not be null or empty.");
            }
            this.resourceName = resourceName;
            return this;
        }

        public ResourceBuilder openingTime(String openingTime) throws Exception {
            if (openingTime == null || openingTime.length() != 5) {
                throw new Exception("Opening time should be in HH:MM format.");
            }
            this.openingTime = openingTime;
            return this;
        }

        public ResourceBuilder closingTime(String closingTime) throws Exception {
            if (closingTime == null || closingTime.length() != 5) {
                throw new Exception("Closing time should be in HH:MM format.");
            }
            this.closingTime = closingTime;
            return this;
        }

        public ResourceBuilder timeslotLength(int timeslotLength) throws Exception {
            if (timeslotLength <= 0 || timeslotLength > 1440) {
                throw new Exception("Timeslot length should be between 1 and 1440 minutes.");
            }
            this.timeslotLength = timeslotLength;
            return this;
        }

        public ResourceBuilder coordinates(ArrayList<int[]> coordinates) throws Exception {
            if (coordinates == null) {
                throw new Exception("Coordinates should not be null.");
            }
            for(int[] coordinate : coordinates) {
                if (coordinate == null) {
                    throw new Exception("Individual coordinates should not be null.");
                }
                if (coordinate.length != 2) {
                    throw new Exception("Each coordinate should have exactly 2 values (row, column).");
                }
            }
            this.coordinates = coordinates;
            return this;
        }

        public Resource build() {
            return new Resource(resourceName, openingTime, closingTime, timeslotLength, coordinates);
        }
    }

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

    public boolean deleteBooking(Booking booking) {
        checkResource();
        Preconditions.checkNotNull(booking, "Booking cannot be null");

        boolean removed = bookings.remove(booking);

        checkResource();
        return removed;
    }

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