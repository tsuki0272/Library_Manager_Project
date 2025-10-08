package ca.umanitoba.cs.longkuma.domain_model.resource;

import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class Resource {
    final private ArrayList<Booking> bookings;

    public Resource() {
        this.bookings = new ArrayList<>();
        checkResource();
    }

    private void checkResource() {
        Preconditions.checkState(bookings != null, "Bookings list should not be null.");

        for (Booking booking : bookings) {
            Preconditions.checkState(booking != null, "Individual bookings should never be null.");
        }
    }


    public ArrayList<Booking> getBookings() {
        checkResource();
        return bookings;
    }

    public boolean addBooking(Booking booking) {
        checkResource();
        Preconditions.checkNotNull(booking, "Booking cannot be null");

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
}
