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

    public Media(String title, String author, String type) {
        this.title = title;
        this.author = author;
        this.type = type;
        this.reviews = new ArrayList<>();
        this.copies = new ArrayList<>();
        this.waitlist = new LinkedList<>();
        checkMedia();
    }

    private void checkMedia() {
        Preconditions.checkState(title != null, "Media title should not be null.");
        Preconditions.checkState(title.length() >= 1, "Media title should have at least one symbol.");
        Preconditions.checkState(author != null, "Media author should not be null.");
        Preconditions.checkState(author.length() >= 1, "Media author should have at least one symbol.");
        Preconditions.checkState(author != null, "Media type should not be null.");
        Preconditions.checkState(author.length() >= 1, "Media type should have at least one symbol.");
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
        MediaCopy availableCopy = getAvailableCopy();
        if (availableCopy != null) {
            String dueDate = "25/12/25"; // Christmas!
            String dueTime = "23:59";
            availableCopy.borrowCopy(member, dueTime, dueDate);
        }
        return availableCopy;
    }


    public boolean addToWaitlist(Member member) {
        checkMedia();
        Preconditions.checkNotNull(member, "Member cannot be null");
        boolean added = waitlist.offer(member);
        checkMedia();
        return added;
    }
}
