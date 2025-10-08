package ca.umanitoba.cs.longkuma.domain_model.media;

import ca.umanitoba.cs.longkuma.domain_model.member.Member;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Media {
    private ArrayList<Review> reviews;
    private ArrayList<MediaCopy> copies;
    private Queue<Member> waitlist; //TODO

    public Media() {
        this.reviews = new ArrayList<>();
        this.copies = new ArrayList<>();
        this.waitlist = new LinkedList<>();
        checkMedia();
    }

    private void checkMedia() {
        Preconditions.checkState(reviews != null, "Reviews list should not be null.");
        Preconditions.checkState(copies != null, "Copies list should not be null.");
        Preconditions.checkState(waitlist != null, "Waitlist should not be null.");

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

    public boolean addToWaitlist(Member member) {
        checkMedia();
        Preconditions.checkNotNull(member, "Member cannot be null");
        boolean added = waitlist.offer(member);
        checkMedia();
        return added;
    }
}
