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

    /*
     * Private constructor for Resource
     * Initializes resource with builder parameters and generates a unique ID
     * Creates an empty bookings list
     *
     * @param builder The ResourceBuilder containing configuration parameters
     */
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

        /*
         * Sets the resource name for the resource being built
         *
         * @param name The name of the resource
         * @return ResourceBuilder instance for method chaining
         */
        public ResourceBuilder resourceName(String name) {
            this.resourceName = name;
            return this;
        }

        /*
         * Sets the opening time for the resource being built
         *
         * @param opening The opening time in HH:MM format
         * @return ResourceBuilder instance for method chaining
         */
        public ResourceBuilder openingTime(String opening) {
            this.openingTime = opening;
            return this;
        }

        /*
         * Sets the closing time for the resource being built
         *
         * @param closing The closing time in HH:MM format
         * @return ResourceBuilder instance for method chaining
         */
        public ResourceBuilder closingTime(String closing) {
            this.closingTime = closing;
            return this;
        }

        /*
         * Sets the timeslot length for the resource being built
         *
         * @param length The length of each timeslot in minutes
         * @return ResourceBuilder instance for method chaining
         */
        public ResourceBuilder timeslotLength(int length) {
            this.timeslotLength = length;
            return this;
        }

        /*
         * Sets the coordinates for the resource being built
         *
         * @param coords The list of coordinate arrays representing resource locations
         * @return ResourceBuilder instance for method chaining
         */
        public ResourceBuilder coordinates(List<int[]> coords) {
            this.coordinates = coords;
            return this;
        }

        /*
         * Builds and returns a new Resource instance with configured parameters
         *
         * @return A new Resource object
         */
        public Resource build() {
            return new Resource(this);
        }
    }

    // Getters:
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

    /*
     * Adds a booking to this resource's collection
     *
     * @param booking The booking to add
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    /*
     * Checks if this resource is available for booking
     *
     * @return true if the resource has no bookings, false otherwise
     */
    public boolean isBookable() {
        return bookings.isEmpty();
    }
}