package ca.umanitoba.cs.longkuma.ui.resource;

import ca.umanitoba.cs.longkuma.domain.resource.Booking;
import ca.umanitoba.cs.longkuma.domain.resource.Resource;

import java.util.ArrayList;
import java.util.List;

public class ResourceDisplay {
    final private Resource resource;

    /**
     * Constructs a ResourceDisplay for the specified resource
     *
     * @param resource The resource to display information for
     */
    public ResourceDisplay(Resource resource) {
        this.resource = resource;
    }

    /**
     * Prints all current bookings for the resource on a specific date
     * Takes in a String in format "DD/MM/YY" and prints all current bookings for that date
     *
     * @param bookingDate The date to display bookings for in "DD/MM/YY" format
     */
    public void printBookings(String bookingDate) {
        if (bookingDate == null || bookingDate.length() != 8) {
            System.out.println("Invalid date format. Use DD/MM/YY");
            return;
        }

        List<Booking> bookings = resource.getBookings();
        int day = Integer.parseInt(bookingDate.substring(0,2));
        int month = Integer.parseInt(bookingDate.substring(3,5));
        int year = Integer.parseInt(bookingDate.substring(6,8)) + 2000;

        System.out.println(resource.getResourceName() + ": ");
        System.out.println("--------------------");

        boolean hasBookings = false;
        for(Booking booking : bookings) {
            if(booking.getDay() == day && booking.getMonth() == month && booking.getYear() == year) {
                System.out.println("Booked from: " + booking.getStartTime() + " - " + booking.getEndTime());
                hasBookings = true;
            }
        }

        if (!hasBookings) {
            System.out.println("No bookings for this date.");
        }

        System.out.println("--------------------");
    }
}