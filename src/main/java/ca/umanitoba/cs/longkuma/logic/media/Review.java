package ca.umanitoba.cs.longkuma.logic.media;

import com.google.common.base.Preconditions;

public class Review {
    final private String review;

    private Review(String review) {
        this.review = review;
        checkReview();
    }

    public static class ReviewBuilder {
        private String review;

        public ReviewBuilder() {}

        public ReviewBuilder review(String review) throws Exception {
            if (review == null || review.isEmpty()) {
                throw new Exception("Review should not be null or empty.");
            }
            this.review = review;
            return this;
        }

        public Review build() {
            return new Review(review);
        }
    }

    private void checkReview() {
        Preconditions.checkState(review != null, "Review should not be null.");
        Preconditions.checkState(review.length() >= 1, "Review should have at least one symbol.");
    }

    public String getReview() {
        checkReview();
        return review;
    }
}
