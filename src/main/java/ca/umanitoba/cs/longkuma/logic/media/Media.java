package ca.umanitoba.cs.longkuma.logic.media;

import ca.umanitoba.cs.longkuma.logic.member.Member;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Media {
    final private String title;
    final private String author;
    final private String type;
    final private ArrayList<Review> reviews;
    final private ArrayList<MediaCopy> copies;
    final private Queue<Member> waitlist;
    final private int[] coordinates;

    private Media(String title, String author, String type, int[] coordinates) {
        this.title = title;
        this.author = author;
        this.type = type;
        this.reviews = new ArrayList<>();
        this.copies = new ArrayList<>();
        this.waitlist = new LinkedList<>();
        this.coordinates = coordinates;
        checkMedia();
    }

    public static class MediaBuilder {
        private String title;
        private String author;
        private String type;
        private int[] coordinates;

        public MediaBuilder() {}

        public MediaBuilder title(String title) throws Exception {
            if (title == null || title.isEmpty()) {
                throw new Exception("Media title should not be null or empty.");
            }
            this.title = title;
            return this;
        }

        public MediaBuilder author(String author) throws Exception {
            if (author == null || author.isEmpty()) {
                throw new Exception("Media author should not be null or empty.");
            }
            this.author = author;
            return this;
        }

        public MediaBuilder type(String type) throws Exception {
            if (type == null || type.isEmpty()) {
                throw new Exception("Media type should not be null or empty.");
            }
            this.type = type;
            return this;
        }

        public MediaBuilder coordinates(int[] coordinates) throws Exception {
            if (coordinates == null) {
                throw new Exception("Media coordinates should not be null.");
            }
            this.coordinates = coordinates;
            return this;
        }

        public Media build() {
            return new Media(title, author, type, coordinates);
        }
    }

    private void checkMedia() {
        Preconditions.checkState(title != null, "Media title should not be null.");
        Preconditions.checkState(title.length() >= 1, "Media title should have at least one symbol.");
        Preconditions.checkState(author != null, "Media author should not be null.");
        Preconditions.checkState(author.length() >= 1, "Media author should have at least one symbol.");
        Preconditions.checkState(type != null, "Media type should not be null.");
        Preconditions.checkState(type.length() >= 1, "Media type should have at least one symbol.");
        Preconditions.checkState(reviews != null, "Reviews list should not be null.");
        Preconditions.checkState(copies != null, "Copies list should not be null.");
        Preconditions.checkState(waitlist != null, "Waitlist should not be null.");
        Preconditions.checkState(coordinates != null, "Coordinates should not be null.");
        Preconditions.checkState(coordinates.length == 2, "Each coordinate should have exactly 2 values (row, column).");

        for (Review review : reviews) {
            Preconditions.checkState(review != null, "Individual reviews should never be null.");
        }

        for (MediaCopy copy : copies) {
            Preconditions.checkState(copy != null, "Individual media copies should never be null.");
        }

        for (Member member : waitlist) {
            Preconditions.checkState(member != null, "Individual members in waitlist should never be null.");
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getType() {
        return type;
    }

    public boolean addReview(Review review) {
        checkMedia();
        Preconditions.checkNotNull(review, "Review cannot be null");
        boolean added = reviews.add(review);
        checkMedia();
        return added;
    }

    public boolean deleteReview(Review review) {
        checkMedia();
        Preconditions.checkNotNull(review, "Review cannot be null");
        boolean removed = reviews.remove(review);
        checkMedia();
        return removed;
    }

    public Review showReview(Review review) {
        checkMedia();
        Preconditions.checkNotNull(review, "Review cannot be null");
        Review foundReview = null;
        for (Review r : reviews) {
            if (r.equals(review)) {
                foundReview = r;
                break;
            }
        }
        checkMedia();
        return foundReview;
    }

    public boolean addCopy(MediaCopy copy) {
        checkMedia();
        Preconditions.checkNotNull(copy, "Media copy cannot be null");
        boolean added = copies.add(copy);
        checkMedia();
        return added;
    }

    public MediaCopy getAvailableCopy() {
        checkMedia();
        MediaCopy availableCopy = null;
        boolean found = false;
        int index = 0;

        while (!found && index < copies.size()) {
            MediaCopy currentCopy = copies.get(index);
            if (currentCopy.isAvailable()) {
                availableCopy = currentCopy;
                found = true;
            }
            index++;
        }

        checkMedia();
        return availableCopy;
    }

    public boolean isAvailable() {
        checkMedia();
        boolean available = false;
        int index = 0;

        while (!available && index < copies.size()) {
            MediaCopy currentCopy = copies.get(index);
            if (currentCopy.isAvailable()) {
                available = true;
            }
            index++;
        }

        checkMedia();
        return available;
    }

    public MediaCopy borrowCopy(Member member) {
        Preconditions.checkNotNull(member, "Member cannot be null");

        // Check if member has constraints - cannot borrow if constrained
        if (member.hasConstraints()) {
            return null;
        }

        MediaCopy availableCopy = getAvailableCopy();
        if (availableCopy != null) {
            String dueDate = "25/12/25"; // Christmas!
            String dueTime = "23:59";
            availableCopy.borrowCopy(member, dueTime, dueDate);
        }
        return availableCopy;
    }

    public void processWaitlist(MediaCopy returnedCopy) {
        checkMedia();
        Preconditions.checkNotNull(returnedCopy, "Returned copy cannot be null");

        // Check if anyone is waiting and if copy is available
        if (!waitlist.isEmpty() && returnedCopy.isAvailable()) {
            Member nextMember = waitlist.poll();

            boolean borrowed = nextMember.borrowMedia(this);

            // If member couldn't borrow (e.g., has constraints), try next person
            if (!borrowed) {
                processWaitlist(returnedCopy);
            }
        }

        checkMedia();
    }

    public boolean removeFromWaitlist(Member member) {
        checkMedia();
        Preconditions.checkNotNull(member, "Member cannot be null");
        boolean removed = waitlist.remove(member);
        checkMedia();
        return removed;
    }

    public boolean addToWaitlist(Member member) {
        checkMedia();
        Preconditions.checkNotNull(member, "Member cannot be null");
        boolean added = waitlist.offer(member);
        checkMedia();
        return added;
    }

    public Queue<Member> getWaitlist() {
        return waitlist;
    }
}