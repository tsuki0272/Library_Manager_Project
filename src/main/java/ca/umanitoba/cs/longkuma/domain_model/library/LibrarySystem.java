package ca.umanitoba.cs.longkuma.domain_model.library;

import ca.umanitoba.cs.longkuma.domain_model.member.Member;
import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class LibrarySystem {
    final private ArrayList<Library> libraries;
    final private ArrayList<Member> members;

    public LibrarySystem() {
        this.libraries = new ArrayList<>();
        this.members = new ArrayList<>();
        checkLibrarySystem();
    }

    private void checkLibrarySystem() {
        Preconditions.checkNotNull(libraries, "Libraries list should not be null.");
        Preconditions.checkNotNull(members, "Members list should not be null.");

        for(Library lib : libraries) {
            Preconditions.checkNotNull(lib,
                    "Individual libraries should never be null.");
        }

        for(Member member : members) {
            Preconditions.checkNotNull(member,
                    "Individual members should never be null.");
        }
    }

    public boolean addLibrary(String newLibName) {
        checkLibrarySystem();
        Library newLib = new Library(newLibName);
        boolean add = true;
        for(Library lib : libraries) {
            if(lib.getName().equalsIgnoreCase(newLib.getName())) {
                add = false; // already exists
                break;
            }
        }
        if(add) {
            libraries.add(newLib);
        }
        checkLibrarySystem();
        return add;
    }

    public Library showLibrary(String libName) {
        checkLibrarySystem();
        Library foundLib = null;
        for (Library lib : libraries) {
            if (lib.getName().equalsIgnoreCase(libName)) {
                foundLib = lib;
                break;
            }
        }
        checkLibrarySystem();
        return foundLib; // return Member if found, null otherwise
    }

    public boolean addMember(Member newMember) {
        checkLibrarySystem();
        boolean add = true;

        for(Member member : members) {
            if(member.getName().equalsIgnoreCase(newMember.getName())) {
                add = false; // already exists
                break;
            }
        }
        if(add) {
            members.add(newMember);
        }
        checkLibrarySystem();
        return add;
    }

    public Member showMember(String memberName) {
        checkLibrarySystem();
        Member foundMember = null;
        for (Member member : members) {
            if (member.getName().equalsIgnoreCase(memberName)) {
                foundMember = member;
                break;
            }
        }
        checkLibrarySystem();
        return foundMember; // return Member if found, null otherwise
    }
}
