package ca.umanitoba.cs.longkuma.logic.member;

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

        public MemberBuilder name(String name) throws Exception {
            if(name == null || name.isEmpty()) {
                throw new Exception("Name should not be null or empty.");
            }
            this.name = name;
            return this;
        }

        public MemberBuilder password(String password) throws Exception {
            if(password == null || password.isEmpty()) {
                throw new Exception("Name should not be null or empty.");
            }
            this.password = password;
            return this;
        }

        public Member build() {
            return new Member(name, password);
        }
    }

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

    public boolean bookResource(Resource resource, Booking booking) {
        checkMember();
        Preconditions.checkNotNull(resource, "Resource cannot be null");
        resource.addBooking(booking);
        boolean added = resources.add(resource);
        checkMember();
        return added;
    }

    public ArrayList<MediaCopy> getBorrowedMedia() {
        return borrowedMedia;
    }

    public boolean addConstraint(Constraint constraint) {
        checkMember();
        Preconditions.checkNotNull(constraint, "Constraint cannot be null");
        boolean added = constraints.add(constraint);
        checkMember();
        return added;
    }

    public boolean hasConstraints() {
        return constraints.size() > 0;
    }
}
