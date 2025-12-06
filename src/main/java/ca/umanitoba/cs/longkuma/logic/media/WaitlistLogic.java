package ca.umanitoba.cs.longkuma.logic.media;

import ca.umanitoba.cs.longkuma.domain.media.Media;
import ca.umanitoba.cs.longkuma.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

public class WaitlistLogic {

    private static final WaitlistLogic instance = new WaitlistLogic();

    /*
     * Returns the singleton instance of WaitlistLogic
     *
     * @return The singleton WaitlistLogic instance
     */
    public static WaitlistLogic getInstance() { return instance; }

    private final List<Entry> entries = new ArrayList<>();

    /*
     * Private inner class representing a waitlist entry
     * Associates a media item with a member waiting for it
     */
    private static class Entry {
        Media media;
        Member member;

        /*
         * Constructs an Entry with the specified media and member
         *
         * @param m The media item being waited for
         * @param mem The member on the waitlist
         */
        Entry(Media m, Member mem) {
            this.media = m;
            this.member = mem;
        }
    }

    /*
     * Adds a member to the waitlist for a specific media item
     *
     * @param media The media item to wait for
     * @param member The member joining the waitlist
     */
    public void add(Media media, Member member) {
        entries.add(new Entry(media, member));
    }

    /*
     * Removes and returns the first member waiting for a specific media item
     * Searches through the waitlist in order and removes the first matching entry
     *
     * @param media The media item to check the waitlist for
     * @return The first member waiting for the media, or null if no one is waiting
     */
    public Member pop(Media media) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).media == media) {
                Member m = entries.get(i).member;
                entries.remove(i);
                return m;
            }
        }
        return null;
    }
}