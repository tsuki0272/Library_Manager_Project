package ca.umanitoba.cs.longkuma.logic.media;

import ca.umanitoba.cs.longkuma.logic.exceptions.InvalidMediaException;
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

        /**
         * Constructs a new MediaBuilder instance
         */
        public MediaBuilder() {}

        /**
         * Sets the title for the media being built
         *
         * @param title The title of the media
         * @return The MediaBuilder instance for method chaining
         * @throws InvalidMediaException if title is null or empty
         */
        public MediaBuilder title(String title) throws InvalidMediaException {
            if (title == null || title.isEmpty()) {
                throw new InvalidMediaException("Media title should not be null or empty.");
            }
            this.title = title;
            return this;
        }

        /**
         * Sets the author for the media being built
         *
         * @param author The author of the media
         * @return The MediaBuilder instance for method chaining
         * @throws InvalidMediaException if author is null or empty
         */
        public MediaBuilder author(String author) throws InvalidMediaException {
            if (author == null || author.isEmpty()) {
                throw new InvalidMediaException("Media author should not be null or empty.");
            }
            this.author = author;
            return this;
        }

        /**
         * Sets the type for the media being built
         *
         * @param type The type of the media
         * @return The MediaBuilder instance for method chaining
         * @throws InvalidMediaException if type is null or empty
         */
        public MediaBuilder type(String type) throws InvalidMediaException {
            if (type == null || type.isEmpty()) {
                throw new InvalidMediaException("Media type should not be null or empty.");
            }
            this.type = type;
            return this;
        }

        /**
         * Sets the coordinates for the media being built
         *
         * @param coordinates The coordinates where the media is located
         * @return The MediaBuilder instance for method chaining
         * @throws InvalidMediaException if coordinates is null
         */
        public MediaBuilder coordinates(int[] coordinates) throws InvalidMediaException {
            if (coordinates == null) {
                throw new InvalidMediaException("Media coordinates should not be null.");
            }
            this.coordinates = coordinates;
            return this;
        }

        /**
         * Builds and returns a new Media instance with the configured properties
         *
         * @return A new Media instance
         */
        public Media build() {
            return new Media(title, author, type, coordinates);
        }
    }

    /**
     * Validates the state of the Media object and its components
     */
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

    // Getters:
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getType() {
        return type;
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

    public int[] getCoordinates() {
        return coordinates;
    }

    public Queue<Member> getWaitlist() {
        return waitlist;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    /**
     * Adds a review to the media
     *
     * @param review The review to add
     * @return true if review was successfully added, false otherwise
     */
    public boolean addReview(Review review) {
        checkMedia();
        Preconditions.checkNotNull(review, "Review cannot be null");
        boolean added = reviews.add(review);
        checkMedia();
        return added;
    }

    /**
     * Deletes a review from the media
     *
     * @param review The review to delete
     * @return true if review was successfully removed, false otherwise
     */
    public boolean deleteReview(Review review) {
        checkMedia();
        Preconditions.checkNotNull(review, "Review cannot be null");
        boolean removed = reviews.remove(review);
        checkMedia();
        return removed;
    }

    /**
     * Searches for and returns a specific review
     *
     * @param review The review to search for
     * @return The found review, or null if not found
     */
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

    /**
     * Adds a copy of the media to the inventory
     *
     * @param copy The media copy to add
     * @return true if copy was successfully added, false otherwise
     */
    public boolean addCopy(MediaCopy copy) {
        checkMedia();
        Preconditions.checkNotNull(copy, "Media copy cannot be null");
        boolean added = copies.add(copy);
        checkMedia();
        return added;
    }

    /**
     * Checks if any copy of the media is available for borrowing
     *
     * @return true if at least one copy is available, false otherwise
     */
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

    /**
     * Borrows a copy of the media for a member
     *
     * @param member The member who wants to borrow the media
     * @return The borrowed MediaCopy, or null if no copy available or member has constraints
     */
    public MediaCopy borrowCopy(Member member) {
        Preconditions.checkNotNull(member, "Member cannot be null");
        MediaCopy availableCopy = null;
        // Check if member has constraints - cannot borrow if constrained
        if (!member.hasConstraints()) {
            availableCopy = getAvailableCopy();
            if (availableCopy != null) {
                String dueDate = "25/12/25"; // Christmas!
                String dueTime = "23:59";
                availableCopy.borrowCopy(member, dueTime, dueDate);
            }
        }
        return availableCopy;
    }

    /**
     * Processes the waitlist when a copy is returned
     *
     * @param returnedCopy The copy that was returned and is now available
     */
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

    /**
     * Adds a member to the waitlist
     *
     * @param member The member to add to the waitlist
     * @return true if member was successfully added, false otherwise
     */
    public boolean addToWaitlist(Member member) {
        checkMedia();
        Preconditions.checkNotNull(member, "Member cannot be null");
        boolean added = waitlist.offer(member);
        checkMedia();
        return added;
    }
}