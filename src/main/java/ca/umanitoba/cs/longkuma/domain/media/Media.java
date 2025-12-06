package ca.umanitoba.cs.longkuma.domain.media;

import java.util.ArrayList;
import java.util.List;

public class Media {

    private final String title;
    private final String author;
    private final int[] coordinates; // <— Required for map
    private final List<MediaCopy> copies;
    private final List<Review> reviews;

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

        public MediaBuilder mediaId(String mediaId) {
            this.mediaId = mediaId;
            return this;
        }

        public MediaBuilder title(String title) {
            this.title = title;
            return this;
        }

        public MediaBuilder author(String author) {
            this.author = author;
            return this;
        }

        public MediaBuilder type(String category) {
            this.category = category;
            return this;
        }

        public MediaBuilder coordinates(int[] coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Media build() {
            if (mediaId == null) {
                mediaId = "M_" + title.replaceAll("\\s+", "_");
            }
            return new Media(mediaId, title, author, category, coordinates);
        }
    }

    public void addCopy(MediaCopy copy) {
        copies.add(copy);
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public boolean addReview(Review r) {
        if (r == null) return false;
        return reviews.add(r);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<MediaCopy> getCopies() {
        return copies;
    }

    public MediaCopy findAvailableCopy() {
        for (MediaCopy copy : copies) {
            if (copy.isAvailable()) {
                return copy;
            }
        }
        return null;
    }
}
