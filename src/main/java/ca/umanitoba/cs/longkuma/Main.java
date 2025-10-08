package ca.umanitoba.cs.longkuma;

import ca.umanitoba.cs.longkuma.domain_model.library.LibrarySystem;
import ca.umanitoba.cs.longkuma.domain_model.library.Library;
import ca.umanitoba.cs.longkuma.domain_model.member.Member;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean quit = false;
        Scanner scnr = new Scanner(System.in);
        String line;
        LibrarySystem librarySystem = new LibrarySystem();
        while(!quit) {
            System.out.println("""
                    === Library System REPL ===
                    Available commands (type \'q\' to exit):
                    1. addLibrary
                    2. showLibrary
                    3. addMember
                    4. showMember
                    Enter a command:""");
            line = scnr.nextLine();
            switch(line){
                case "1":
                    System.out.println("Command chosen: addLibrary()");
                    System.out.print("ENTER NEW LIBRARY NAME: ");
                    line = scnr.nextLine();
                    boolean addedLibrary = librarySystem.addLibrary(line);
                    if(addedLibrary) {
                        System.out.println("Successfully added!");
                    } else {
                        System.out.println("ERROR: A library with this name already exists!");
                    }
                    break;
                case "2":
                    System.out.println("Command chosen: showLibrary()");
                    System.out.print("ENTER LIBRARY NAME TO SHOW: ");
                    line = scnr.nextLine();
                    Library lib = librarySystem.showLibrary(line);
                    if (lib != null) {
                        System.out.println("Library found: " + lib.getName());
                    } else {
                        System.out.println("ERROR: Library not found!");
                    }
                    break;
                case "3":
                    System.out.println("Command chosen: addMember()");
                    System.out.print("ENTER NEW MEMBER NAME: ");
                    line = scnr.nextLine();
                    boolean addedMember = librarySystem.addMember(line);
                    if(addedMember) {
                        System.out.println("Successfully added!");
                    } else {
                        System.out.println("ERROR: A member with this name already exists!");
                    }
                    break;
                case "4":
                    System.out.println("Command chosen: showMember()");
                    System.out.print("ENTER MEMBER NAME TO SHOW: ");
                    line = scnr.nextLine();
                    Member member = librarySystem.showMember(line);
                    if (member != null) {
                        System.out.println("Member found: " + member.getName());
                    } else {
                        System.out.println("ERROR: Member not found!");
                    }
                    break;
                case "q":
                    scnr.close();
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid input. Try again.");
                    break;
            }
        }

    }
}