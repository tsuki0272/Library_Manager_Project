package ca.umanitoba.cs.longkuma.domain.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Resource {

    private final String id;
    private final String resourceName;
    private final String openingTime;
    private final String closingTime;
    private final int timeslotLength;
    private final List<int[]> coordinates;
    private final List<Booking> bookings;

    private Resource(ResourceBuilder builder) {
        this.id = UUID.randomUUID().toString(); // NEW
        this.resourceName = builder.resourceName;
        this.openingTime = builder.openingTime;
        this.closingTime = builder.closingTime;
        this.timeslotLength = builder.timeslotLength;
        this.coordinates = builder.coordinates;
        this.bookings = new ArrayList<>();
    }

    public static class ResourceBuilder {
        private String resourceName;
        private String openingTime;
        private String closingTime;
        private int timeslotLength;
        private List<int[]> coordinates = new ArrayList<>();

        public ResourceBuilder resourceName(String name) {
            this.resourceName = name;
            return this;
        }

        public ResourceBuilder openingTime(String opening) {
            this.openingTime = opening;
            return this;
        }

        public ResourceBuilder closingTime(String closing) {
            this.closingTime = closing;
            return this;
        }

        public ResourceBuilder timeslotLength(int length) {
            this.timeslotLength = length;
            return this;
        }

        public ResourceBuilder coordinates(List<int[]> coords) {
            this.coordinates = coords;
            return this;
        }

        public Resource build() {
            return new Resource(this);
        }
    }

    // ======== GETTERS ========
    public String getId() {
        return id;
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

    public List<int[]> getCoordinates() {
        return coordinates;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    // Domain-only add
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    // Domain hint (logic may still check time conflicts)
    public boolean isBookable() {
        return bookings.isEmpty();
    }
}
