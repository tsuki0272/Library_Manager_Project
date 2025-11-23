package ca.umanitoba.cs.longkuma.logic.media;
import ca.umanitoba.cs.longkuma.logic.member.Member;
import com.google.common.base.Preconditions;

import java.util.Scanner;

public class MediaCopy {
    final private int copyNumber;
    final private Media media;
    private boolean isAvailable;
    private Member currentBorrower;
    private String dueTime; // Format: "24:00"
    private String dueDate; // Format: "dd/mm/yy"

    private MediaCopy(int copyNumber, Media media) {
        this.copyNumber = copyNumber;
        this.media = media;
        this.isAvailable = true;
        this.currentBorrower = null;
        this.dueTime = null;
        this.dueDate = null;
        checkMediaCopy();
    }

    public static class MediaCopyBuilder {
        private int copyNumber;
        private Media media;

        public MediaCopyBuilder() {}

        public MediaCopyBuilder copyNumber(int copyNumber) throws Exception {
            if (copyNumber < 1) {
                throw new Exception("Copy number should be at least 1.");
            }
            this.copyNumber = copyNumber;
            return this;
        }

        public MediaCopyBuilder media(Media media) throws Exception {
            if (media == null) {
                throw new Exception("Media should not be null.");
            }
            this.media = media;
            return this;
        }

        public MediaCopy build() {
            return new MediaCopy(copyNumber, media);
        }
    }

    private void checkMediaCopy() {
        Preconditions.checkState(copyNumber >= 1, "Copy number should be at least 1.");
        Preconditions.checkState(media != null, "Media should not be null.");

        if(!isAvailable) {
            Preconditions.checkState(currentBorrower != null, "Borrowed copy must have a current borrower.");
            Preconditions.checkState(dueDate != null, "Borrowed copy must have a due date.");
            Preconditions.checkState(isValidDueDate(dueDate), "Due date must be in format '24:00,dd/mm/yy'");
        }
    }

    private boolean isValidDueDate(String date) {
        boolean isValid = false;
        if(date != null) {
            Scanner scanner = new Scanner(date);
            if(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split("/");
                if (tokens.length == 3) {
                    int day = -1;
                    int month = -1;
                    try {
                        day = Integer.parseInt(tokens[0]);
                        month = Integer.parseInt(tokens[1]);
                    } catch (NumberFormatException nfe) {
                        // Parsing failed, values remain -1
                        System.err.println("Due date is not in valid format. it must be in dd/mm/yy format.");
                    }
                    if (day > 0 && day <= 31 && month > 0 && month <= 12) {
                        isValid = true;
                    }
                }
            }
            scanner.close();
        }
        return isValid;
    }

    private boolean isValidDueTime(String time) {
        boolean isValid = false;

        if (time != null) {
            Scanner scanner = new Scanner(time);

            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(":");

                if (parts.length == 2) {
                    int hours = -1;
                    int minutes = -1;

                    try {
                        hours = Integer.parseInt(parts[0]);
                        minutes = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException e) {
                        // Parsing failed, values remain -1
                    }

                    if (hours >= 0 && hours < 24 && minutes >= 0 && minutes < 60) {
                        isValid = true;
                    }
                }
            }

            scanner.close();
        }

        return isValid;
    }

    public void borrowCopy(Member member, String dueTime, String dueDate) {
        if(isAvailable && isValidDueTime(dueTime) && isValidDueDate(dueDate)) {
            this.currentBorrower = member;
            this.dueTime = dueTime;
            this.dueDate = dueDate;
            isAvailable = false;
        }
    }

    public void returnCopy() {
        checkMediaCopy();
        if (!isAvailable) {
            this.isAvailable = true;
            this.currentBorrower = null;
            this.dueTime = null;
            this.dueDate = null;
            // Process waitlist - give to next person waiting
            media.processWaitlist(this);
        }
        checkMediaCopy();
    }

    public int getCopyNumber() {
        checkMediaCopy();
        return copyNumber;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Media getMedia() {
        return media;
    }
}
