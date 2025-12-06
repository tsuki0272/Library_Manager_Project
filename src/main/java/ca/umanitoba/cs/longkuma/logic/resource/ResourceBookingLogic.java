package ca.umanitoba.cs.longkuma.logic.resource;

import ca.umanitoba.cs.longkuma.domain.resource.Resource;
import ca.umanitoba.cs.longkuma.domain.member.Member;
import java.util.ArrayList;
import java.util.List;

public class ResourceBookingLogic {
    private final List<Resource> allResources = new ArrayList<>();
    private final List<BookingRecord> activeBookings = new ArrayList<>();

    // Add a resource to the system
    public void addResource(Resource resource) {
        allResources.add(resource);
    }

    // Retrieve resource by ID
    public Resource getResource(String id) {
        for (Resource r : allResources) {
            if (r.getId().equals(id)) return r;
        }
        return null;
    }

    // Check if a resource is currently booked
    public boolean isBooked(Resource resource) {
        for (BookingRecord booking : activeBookings) {
            if (booking.resource().getId().equals(resource.getId())) {
                return true;
            }
        }
        return false;
    }

    // Attempt to book a resource
    public boolean bookResource(Member member, Resource resource) {
        if (!resource.isBookable()) return false;
        if (isBooked(resource)) return false;

        activeBookings.add(new BookingRecord(member, resource));
        return true;
    }

    // Release a booking
    public boolean releaseResource(Member member, Resource resource) {
        for (int i = 0; i < activeBookings.size(); i++) {
            BookingRecord booking = activeBookings.get(i);
            if (booking.member().equals(member) && booking.resource().getId().equals(resource.getId())) {
                activeBookings.remove(i);
                return true;
            }
        }
        return false;
    }

    // BookingRecord simple holder class
    public record BookingRecord(Member member, Resource resource) {}
}