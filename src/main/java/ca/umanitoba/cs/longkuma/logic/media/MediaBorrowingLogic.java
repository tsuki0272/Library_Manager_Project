package ca.umanitoba.cs.longkuma.logic.media;

import ca.umanitoba.cs.longkuma.domain.media.Media;
import ca.umanitoba.cs.longkuma.domain.media.MediaCopy;
import ca.umanitoba.cs.longkuma.domain.member.Member;

public class MediaBorrowingLogic {

    private static final MediaBorrowingLogic instance = new MediaBorrowingLogic();

    /*
     * Returns the singleton instance of MediaBorrowingLogic
     *
     * @return The singleton MediaBorrowingLogic instance
     */
    public static MediaBorrowingLogic getInstance() { return instance; }

    /*
     * Handles the borrowing of media by a member
     * Checks member constraints, finds available copy, marks it as borrowed
     * If no copies available, adds member to waitlist
     *
     * @param media The media item to borrow
     * @param member The member attempting to borrow
     * @param dueTime The time the media is due to be returned
     * @param dueDate The date the media is due to be returned
     * @return BorrowResult indicating success, waitlist, or failure
     */
    public BorrowResult borrow(Media media, Member member, String dueTime, String dueDate) {

        if (member.hasConstraints())
            return BorrowResult.failure("Member has constraints");

        MediaCopy available = media.findAvailableCopy();

        if (available != null) {
            available.markBorrowed(member, dueTime, dueDate);
            member.addBorrowedCopy(available);
            return BorrowResult.success("Borrowed successfully", available);
        }

        WaitlistLogic.getInstance().add(media, member);
        return BorrowResult.waitlisted("Added to waitlist");
    }

    /*
     * Handles the return of a media copy by a member
     * Validates member has the copy, marks it as returned, checks waitlist for next borrower
     * If someone is on waitlist, automatically borrows copy to them
     *
     * @param copy The media copy being returned
     * @param member The member returning the copy
     * @return BorrowResult indicating success or failure
     */
    public BorrowResult returnCopy(MediaCopy copy, Member member) {

        if (!member.getBorrowedMedia().contains(copy))
            return BorrowResult.failure("Member does not have this copy");

        member.removeBorrowedCopy(copy);
        copy.markReturned();

        Member next = WaitlistLogic.getInstance().pop(copy.getMedia());

        if (next != null) {
            copy.markBorrowed(next, "12:00", "01/01/30");
            next.addBorrowedCopy(copy);

            return BorrowResult.success(
                    "Returned and given to next waitlisted member",
                    copy
            );
        }

        return BorrowResult.success("Returned successfully", copy);
    }

    // ===== DTO =====
    public static class BorrowResult {

        public enum Status { SUCCESS, WAITLISTED, FAILURE }

        private final Status status;
        private final String message;
        private final MediaCopy copy;

        /*
         * Private constructor for BorrowResult
         * Initializes result with status, message, and optional media copy
         *
         * @param s The status of the borrow operation
         * @param msg The message describing the result
         * @param c The media copy involved, or null if not applicable
         */
        private BorrowResult(Status s, String msg, MediaCopy c) {
            status = s;
            message = msg;
            copy = c;
        }

        /*
         * Creates a successful borrow result
         *
         * @param msg The success message
         * @param c The media copy that was borrowed
         * @return A BorrowResult with SUCCESS status
         */
        public static BorrowResult success(String msg, MediaCopy c) {
            return new BorrowResult(Status.SUCCESS, msg, c);
        }

        /*
         * Creates a failed borrow result
         *
         * @param msg The failure message
         * @return A BorrowResult with FAILURE status and null copy
         */
        public static BorrowResult failure(String msg) {
            return new BorrowResult(Status.FAILURE, msg, null);
        }

        /*
         * Creates a waitlisted borrow result
         *
         * @param msg The waitlist message
         * @return A BorrowResult with WAITLISTED status and null copy
         */
        public static BorrowResult waitlisted(String msg) {
            return new BorrowResult(Status.WAITLISTED, msg, null);
        }

        // Getters:
        public Status getStatus() { return status; }
        public String getMessage() { return message; }
        public MediaCopy getCopy() { return copy; }
    }
}