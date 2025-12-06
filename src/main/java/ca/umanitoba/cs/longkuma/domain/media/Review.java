package ca.umanitoba.cs.longkuma.domain.media;

import ca.umanitoba.cs.longkuma.domain.exceptions.InvalidReviewException;
import com.google.common.base.Preconditions;

public class Review {
    final private String review;

    private Review(String review) {
        this.review = review;
        checkReview();
    }

    public static class ReviewBuilder {
        private String review;

        /**
         * Constructs a new ReviewBuilder instance
         */
        public ReviewBuilder() {}

        /**
         * Sets the review text for the review being built
         *
         * @param review The text content of the review
         * @return The ReviewBuilder instance for method chaining
         * @throws Exception if review is null or empty
         */
        public ReviewBuilder review(String review) throws InvalidReviewException {
            if (review == null || review.isEmpty()) {
                throw new InvalidReviewException("Review should not be null or empty.");
            }
            this.review = review;
            return this;
        }

        /**
         * Builds and returns a new Review instance with the configured properties
         *
         * @return A new Review instance
         */
        public Review build() {
            return new Review(review);
        }
    }

    /**
     * Validates the state of the Review object
     */
    private void checkReview() {
        Preconditions.checkState(review != null, "Review should not be null.");
        Preconditions.checkState(review.length() >= 1, "Review should have at least one symbol.");
    }

    public String getReview() {
        checkReview();
        return review;
    }
}