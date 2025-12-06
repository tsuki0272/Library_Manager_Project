package ca.umanitoba.cs.longkuma.logic.library;

import ca.umanitoba.cs.longkuma.domain.library.Library;
import ca.umanitoba.cs.longkuma.domain.member.Member;
import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class LibrarySystem {
    final private ArrayList<Library> libraries;
    final private ArrayList<Member> members;

    /*
     * Private constructor for LibrarySystem
     * Initializes empty lists for libraries and members
     */
    private LibrarySystem() {
        this.libraries = new ArrayList<>();
        this.members = new ArrayList<>();
        checkLibrarySystem();
    }

    public static class LibrarySystemBuilder {
        public LibrarySystemBuilder() {}

        /*
         * Builds and returns a new LibrarySystem instance
         *
         * @return A new LibrarySystem object with empty libraries and members lists
         */
        public LibrarySystem build() {
            return new LibrarySystem();
        }
    }

    /*
     * Validates the internal state of the LibrarySystem object
     * Ensures libraries and members lists are non-null
     * Checks that all libraries and members in collections are non-null
     */
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

    /*
     * Gets the list of all libraries in the system
     *
     * @return ArrayList containing all library objects
     */
    public ArrayList<Library> getLibraries() {
        return libraries;
    }

    /*
     * Adds a new library to the system if no library with the same name exists
     * Library names are compared case-insensitively
     * Validates system state before and after adding
     *
     * @param newLib The library to add
     * @return true if library was added successfully, false if library with same name already exists
     */
    public boolean addLibrary(Library newLib) {
        checkLibrarySystem();
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

    /*
     * Searches for a library by name in the system
     * Search is case-insensitive
     *
     * @param libName The name of the library to search for
     * @return The matching Library object if found, null otherwise
     */
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
        return foundLib;
    }

    /*
     * Adds a new member to the system if no member with the same name exists
     * Member names are compared case-insensitively
     * Validates system state before and after adding
     *
     * @param newMember The member to add
     * @return true if member was added successfully, false if member with same name already exists
     */
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

    /*
     * Searches for a member by name in the system
     * Search is case-insensitive
     *
     * @param memberName The name of the member to search for
     * @return The matching Member object if found, null otherwise
     */
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
        return foundMember;
    }
}