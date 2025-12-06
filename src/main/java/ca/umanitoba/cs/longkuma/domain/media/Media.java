package ca.umanitoba.cs.longkuma.domain.media;

import java.util.ArrayList;
import java.util.List;

public class Media {

    private final String title;
    private final String author;
    private final int[] coordinates;
    private final List<MediaCopy> copies;
    private final List<Review> reviews;

    /*
     * Private constructor for Media
     * Initializes media with title, author, and coordinates, creates empty copies and reviews lists
     *
     * @param mediaId The unique identifier for the media item
     * @param title The title of the media item
     * @param author The author of the media item
     * @param category The category of the media item
     * @param coordinates The location coordinates of the media in the library
     */
    private Media(String mediaId, String title, String author, String category, int[] coordinates) {
        this.title = title;
        this.author = author;
        this.coordinates = coordinates;
        this.copies = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    // ==== BUILDER ====
    public static class MediaBuilder {
        private String mediaId;
        private String title;
        private String author;
        private String category;
        private int[] coordinates;

        /*
         * Sets the media ID for the media being built
         *
         * @param mediaId The unique identifier for the media item
         * @return MediaBuilder instance for method chaining
         */
        public MediaBuilder mediaId(String mediaId) {
            this.mediaId = mediaId;
            return this;
        }

        /*
         * Sets the title for the media being built
         *
         * @param title The title of the media item
         * @return MediaBuilder instance for method chaining
         */
        public MediaBuilder title(String title) {
            this.title = title;
            return this;
        }

        /*
         * Sets the author for the media being built
         *
         * @param author The author of the media item
         * @return MediaBuilder instance for method chaining
         */
        public MediaBuilder author(String author) {
            this.author = author;
            return this;
        }

        /*
         * Sets the type/category for the media being built
         *
         * @param category The category of the media item
         * @return MediaBuilder instance for method chaining
         */
        public MediaBuilder type(String category) {
            this.category = category;
            return this;
        }

        /*
         * Sets the coordinates for the media being built
         *
         * @param coordinates The location coordinates of the media in the library
         * @return MediaBuilder instance for method chaining
         */
        public MediaBuilder coordinates(int[] coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        /*
         * Builds and returns a new Media instance with configured parameters
         * If mediaId is null, generates one from the title
         *
         * @return A new Media object
         */
        public Media build() {
            if (mediaId == null) {
                mediaId = "M_" + title.replaceAll("\\s+", "_");
            }
            return new Media(mediaId, title, author, category, coordinates);
        }
    }

    /*
     * Adds a media copy to this media item's collection
     *
     * @param copy The media copy to add
     */
    public void addCopy(MediaCopy copy) {
        copies.add(copy);
    }

    // Getters:
    public int[] getCoordinates() {
        return coordinates;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<MediaCopy> getCopies() {
        return copies;
    }

    /*
     * Adds a review to this media item's collection
     *
     * @param r The review to add
     * @return true if review was successfully added, false if review is null
     */
    public boolean addReview(Review r) {
        if (r == null) return false;
        return reviews.add(r);
    }

    /*
     * Searches for and returns the first available copy of this media item
     *
     * @return The first available MediaCopy, or null if no copies are available
     */
    public MediaCopy findAvailableCopy() {
        for (MediaCopy copy : copies) {
            if (copy.isAvailable()) {
                return copy;
            }
        }
        return null;
    }
}