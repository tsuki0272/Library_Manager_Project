package ca.umanitoba.cs.longkuma.logic.media;

import ca.umanitoba.cs.longkuma.domain.media.Media;
import ca.umanitoba.cs.longkuma.domain.media.MediaCopy;
import ca.umanitoba.cs.longkuma.domain.member.Member;

public class MediaBorrowingLogic {

    private static final MediaBorrowingLogic instance = new MediaBorrowingLogic();
    public static MediaBorrowingLogic getInstance() { return instance; }

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

        private BorrowResult(Status s, String msg, MediaCopy c) {
            status = s;
            message = msg;
            copy = c;
        }

        public static BorrowResult success(String msg, MediaCopy c) {
            return new BorrowResult(Status.SUCCESS, msg, c);
        }

        public static BorrowResult failure(String msg) {
            return new BorrowResult(Status.FAILURE, msg, null);
        }

        public static BorrowResult waitlisted(String msg) {
            return new BorrowResult(Status.WAITLISTED, msg, null);
        }

        public Status getStatus() { return status; }
        public String getMessage() { return message; }
        public MediaCopy getCopy() { return copy; }
    }
}
