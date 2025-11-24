package ca.umanitoba.cs.longkuma.ui.media;

import ca.umanitoba.cs.longkuma.logic.media.MediaCopy;
public class MediaCopyDisplay {

    MediaCopy copy;

    public MediaCopyDisplay(MediaCopy copy) {
        this.copy = copy;
    }

    public void print() {
        System.out.printf("\"%s\" by %s", copy.getMedia().getTitle(), copy.getMedia().getAuthor());
        System.out.print(", Status: " + (copy.isAvailable() ? "Available" : "Borrowed"));
        System.out.println(", Due Date: " + copy.getDueDate() + ", Due Time: " + copy.getDueTime());
    }
}
