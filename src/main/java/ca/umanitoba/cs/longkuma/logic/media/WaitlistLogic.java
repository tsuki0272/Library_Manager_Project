package ca.umanitoba.cs.longkuma.logic.media;

import ca.umanitoba.cs.longkuma.domain.media.Media;
import ca.umanitoba.cs.longkuma.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

public class WaitlistLogic {

    private static final WaitlistLogic instance = new WaitlistLogic();
    public static WaitlistLogic getInstance() { return instance; }

    private final List<Entry> entries = new ArrayList<>();

    private static class Entry {
        Media media;
        Member member;
        Entry(Media m, Member mem) {
            this.media = m;
            this.member = mem;
        }
    }

    public void add(Media media, Member member) {
        entries.add(new Entry(media, member));
    }

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
