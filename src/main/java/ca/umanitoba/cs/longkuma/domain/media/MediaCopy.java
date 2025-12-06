package ca.umanitoba.cs.longkuma.domain.media;

import ca.umanitoba.cs.longkuma.domain.member.Member;
import com.google.common.base.Preconditions;

public class MediaCopy {

    private final int copyNumber;
    private final Media media;
    private boolean borrowed;
    private Member borrowedBy;
    private String dueTime;
    private String dueDate;

    /*
     * Private constructor for MediaCopy
     * Initializes media copy with copy number and media reference
     * Sets initial state as not borrowed with null borrowing details
     *
     * @param copyNumber The unique copy number for this media copy
     * @param media The media item this copy belongs to
     */
    private MediaCopy(int copyNumber, Media media) {
        this.copyNumber = copyNumber;
        this.media = media;
        this.borrowed = false;
        this.borrowedBy = null;
        this.dueTime = null;
        this.dueDate = null;
        checkMediaCopy();
    }

    public static class MediaCopyBuilder {
        private int copyNumber;
        private Media media;

        /*
         * Sets the copy number for the media copy being built
         *
         * @param copyNumber The unique copy number for this media copy
         * @return MediaCopyBuilder instance for method chaining
         */
        public MediaCopyBuilder copyNumber(int copyNumber) {
            this.copyNumber = copyNumber;
            return this;
        }

        /*
         * Sets the media reference for the media copy being built
         *
         * @param media The media item this copy belongs to
         * @return MediaCopyBuilder instance for method chaining
         */
        public MediaCopyBuilder media(Media media) {
            this.media = media;
            return this;
        }

        /*
         * Builds and returns a new MediaCopy instance with configured parameters
         *
         * @return A new MediaCopy object
         */
        public MediaCopy build() {
            return new MediaCopy(copyNumber, media);
        }
    }

    /*
     * Checks if this media copy is available for borrowing
     *
     * @return true if the copy is not borrowed, false otherwise
     */
    public boolean isAvailable() {
        return !borrowed;
    }

    /*
     * Marks this media copy as borrowed by a member
     * Sets the borrowed status, borrowing member, and due date/time
     *
     * @param member The member borrowing this copy
     * @param dueTime The time the copy is due to be returned
     * @param dueDate The date the copy is due to be returned
     */
    public void markBorrowed(Member member, String dueTime, String dueDate) {
        checkMediaCopy();
        this.borrowed = true;
        this.borrowedBy = member;
        this.dueTime = dueTime;
        this.dueDate = dueDate;
    }

    /*
     * Marks this media copy as returned
     * Resets the borrowed status, borrowing member, and due date/time to initial state
     */
    public void markReturned() {
        checkMediaCopy();
        this.borrowed = false;
        this.borrowedBy = null;
        this.dueTime = null;
        this.dueDate = null;
    }

    // Getters:
    public Media getMedia() {
        return media;
    }

    public String getDueTime() {
        return dueTime;
    }

    public String getDueDate() {
        return dueDate;
    }

    private void checkMediaCopy() {
        Preconditions.checkState(copyNumber > 0);
        Preconditions.checkState(media != null);
        Preconditions.checkState(!borrowed || borrowedBy != null);
        Preconditions.checkState(!borrowed || (dueTime != null && !dueTime.isEmpty()));
        Preconditions.checkState(!borrowed || (dueDate != null && !dueDate.isEmpty()));
    }







}