package ca.umanitoba.cs.longkuma.logic.resource;

import ca.umanitoba.cs.longkuma.logic.member.Member;
import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class Resource {
    final private ArrayList<Booking> bookings; // Can book from 12:00 - 20:00
    final String resourceName;

    public Resource(String resourceName) {
        this.bookings = new ArrayList<>();
        this.resourceName = resourceName;
        checkResource();
    }

    private void checkResource() {
        Preconditions.checkState(bookings != null, "Bookings list should not be null.");
        Preconditions.checkState(resourceName != null, "Resource name should not be null.");
        Preconditions.checkState(resourceName.length() >= 1, "Resource name should have at least one symbol.");

        for (Booking booking : bookings) {
            Preconditions.checkState(booking != null, "Individual bookings should never be null.");
        }
    }

    public String getResourceName() {
        return resourceName;
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

    public boolean addBooking(Booking booking) {
        checkResource();
        Preconditions.checkNotNull(booking, "Booking cannot be null");
        Preconditions.checkState(validBookingFormat(booking), "Booking must be within 12:00 - 20:00");
        Preconditions.checkState(validBookingTime(booking), "Booking must be 2 hours or less");
        Preconditions.checkState(validBookingLimit(booking), "Members can book 1 time a day");
        Preconditions.checkState(availableTimeSlot(booking), "Someone else has booked at this time");

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

    // check if a booking is within the opening hours of the library (12:00 - 20:00)
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

            // Check minutes are 00 (bookings by the hour)
            if (startMinute != 0 || endMinute != 0) {
                isValid = false;
            }

            // Check within operating hours (12:00 to 20:00)
            if (startHour < 12 || endHour > 20) {
                isValid = false;
            }

            // Check start time is before end time
            if (startHour >= endHour) {
                isValid = false;
            }
        }

        return isValid;
    }

    // check if a booking is 2 hours or less
    public boolean validBookingTime(Booking booking) {
        String startTime = booking.getStartTime();
        String endTime = booking.getEndTime();
        int startHour = Integer.parseInt(startTime.substring(0, 2));
        int endHour = Integer.parseInt(endTime.substring(0, 2));
        boolean isValid = true;

        int duration = endHour - startHour;
        if (duration > 2) {
            isValid = false;
        }
        return isValid;
    }

    // check if this member has already booked for this day
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

    // check if someone else has booked at this time
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
                int newStart = Integer.parseInt(newBooking.getStartTime().substring(0, 2));
                int newEnd = Integer.parseInt(newBooking.getEndTime().substring(0, 2));
                int existingStart = Integer.parseInt(booking.getStartTime().substring(0, 2));
                int existingEnd = Integer.parseInt(booking.getEndTime().substring(0, 2));

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

}
