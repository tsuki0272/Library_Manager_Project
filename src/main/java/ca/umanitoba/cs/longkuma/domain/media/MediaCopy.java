package ca.umanitoba.cs.longkuma.domain.media;

import ca.umanitoba.cs.longkuma.domain.member.Member;

public class MediaCopy {

    private final int copyNumber;
    private final Media media;
    private boolean borrowed;
    private Member borrowedBy;
    private String dueTime;
    private String dueDate;

    private MediaCopy(int copyNumber, Media media) {
        this.copyNumber = copyNumber;
        this.media = media;
        this.borrowed = false;
        this.borrowedBy = null;
        this.dueTime = null;
        this.dueDate = null;
    }

    public static class MediaCopyBuilder {
        private int copyNumber;
        private Media media;

        public MediaCopyBuilder copyNumber(int copyNumber) {
            this.copyNumber = copyNumber;
            return this;
        }

        public MediaCopyBuilder media(Media media) {
            this.media = media;
            return this;
        }

        public MediaCopy build() {
            return new MediaCopy(copyNumber, media);
        }
    }

    public boolean isAvailable() {
        return !borrowed;
    }

    public void markBorrowed(Member member, String dueTime, String dueDate) {
        this.borrowed = true;
        this.borrowedBy = member;
        this.dueTime = dueTime;
        this.dueDate = dueDate;
    }

    public void markReturned() {
        this.borrowed = false;
        this.borrowedBy = null;
        this.dueTime = null;
        this.dueDate = null;
    }

    public Media getMedia() {
        return media;
    }

    public String getDueTime() {
        return dueTime;
    }

    public String getDueDate() {
        return dueDate;
    }
}
