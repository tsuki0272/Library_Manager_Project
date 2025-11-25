package ca.umanitoba.cs.longkuma.ui.media;

import ca.umanitoba.cs.longkuma.logic.media.MediaCopy;
public class MediaCopyDisplay {

    MediaCopy copy;

    /**
     * Constructs a MediaCopyDisplay for the specified media copy
     *
     * @param copy The media copy to display
     */
    public MediaCopyDisplay(MediaCopy copy) {
        this.copy = copy;
    }

    /**
     * Prints formatted information about the media copy including:
     * - Title and author
     * - Availability status
     * - Due date and time (if borrowed)
     */
    public void print() {
        System.out.printf("\"%s\" by %s", copy.getMedia().getTitle(), copy.getMedia().getAuthor());
        System.out.print(", Status: " + (copy.isAvailable() ? "Available" : "Borrowed"));
        System.out.println(", Due Date: " + copy.getDueDate() + ", Due Time: " + copy.getDueTime());
    }
}