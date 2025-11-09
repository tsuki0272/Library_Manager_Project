package ca.umanitoba.cs.longkuma.ui;

import ca.umanitoba.cs.longkuma.domain_model.library.Library;
import ca.umanitoba.cs.longkuma.domain_model.library.LibrarySystem;
import ca.umanitoba.cs.longkuma.domain_model.media.Media;
import ca.umanitoba.cs.longkuma.domain_model.member.Member;

import java.util.ArrayList;
import java.util.Scanner;

public class MemberActionsDisplay {
    private final LibrarySystem libSystem;
    private final Member member;
    private final Scanner keyboard;
    private static final String[] memberOptions = {"BOOK RESOURCE", "BORROW MEDIA", "RETURN MEDIA", "SIGN OUT"};
    
    public MemberActionsDisplay(LibrarySystem libSystem, Member member, Scanner keyboard) {
        this.libSystem = libSystem;
        this.member = member;
        this.keyboard = keyboard;
    }
    
    public void showOptions() {
        String task;
        boolean signedOut = false;
        while(!signedOut) {
            System.out.println("=====MAIN MENU=====");
            System.out.println("SELECT AN OPTION: ");
            printOptions();
            task = getInput();
            switch(task) {
                case "BOOK RESOURCE":
                    System.out.println("You chose: BOOK RESOURCE");
                    break;
                case "BORROW MEDIA":
                    System.out.println("You chose: BORROW MEDIA");
                    if(this.member.hasConstraints()) {
                        System.out.println("Sorry, you have constraints that prevent you from borrowing more media.");
                    } else {
                        borrowMedia();
                    }
                    break;
                case "RETURN MEDIA":
                    System.out.println("You chose: RETURN MEDIA");
                    break;
                case "SIGN OUT":
                    System.out.println("You chose: SIGN OUT");
                    System.out.println("You have successfully signed out.");
                    signedOut = true;
                default:
                    System.out.println("I don't know what that means.");
                    break;
            }
        }
    }

    private void borrowMedia() {
        ArrayList<Library> libraries = this.libSystem.getLibraries();
        int libCounter = 1;
        for(Library library : libraries) {
            System.out.println(libCounter + ". " + library.getName());
            ArrayList<Media> media = library.getMedia();
            for(int i = 0; i < media.size(); i++) {
                System.out.printf("%d. \"%s\" by %s\n", i, media.get(i).getTitle(), media.get(i).getAuthor());
            }
            System.out.println();
            libCounter++;
        }
    }

    private static void printOptions() {
        for(int i = 0; i < memberOptions.length; i++) {
            System.out.printf("%s \n", memberOptions[i]);
        }
    }

    private String getInput() {
        String loginInput;
        loginInput = keyboard.nextLine().toUpperCase();
        return loginInput;
    }
}
