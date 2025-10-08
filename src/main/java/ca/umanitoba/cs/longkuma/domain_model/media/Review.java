package ca.umanitoba.cs.longkuma.domain_model.media;

import com.google.common.base.Preconditions;

public class Review {
    private String review;

    public Review(String review) {
        this.review = review;
        checkReview();
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
