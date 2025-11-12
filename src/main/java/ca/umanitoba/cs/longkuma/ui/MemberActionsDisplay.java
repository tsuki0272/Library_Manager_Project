package ca.umanitoba.cs.longkuma.ui;

import ca.umanitoba.cs.longkuma.logic.library.Library;
import ca.umanitoba.cs.longkuma.logic.library.LibrarySystem;
import ca.umanitoba.cs.longkuma.logic.media.Media;
import ca.umanitoba.cs.longkuma.logic.member.Member;
import ca.umanitoba.cs.longkuma.logic.resource.Booking;
import ca.umanitoba.cs.longkuma.logic.resource.Resource;
import com.google.common.base.Preconditions;

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
                    bookResource();
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
                    returnMedia();
                    break;
                case "SIGN OUT":
                    System.out.println("You chose: SIGN OUT");
                    System.out.println("You have successfully signed out.");
                    signedOut = true;
                    break;
                default:
                    System.out.println("I don't know what that means.");
                    break;
            }
        }
    }

    private void showResources() {
        ArrayList<Library> libraries = this.libSystem.getLibraries();
        int libCounter = 1;
        for(Library library : libraries) {
            System.out.println(libCounter + ". " + library.getName());
            ArrayList<Resource> resources = library.getResources();
            for(int i = 0; i < resources.size(); i++) {
                System.out.printf("\t%d. \"%s\"\n", i + 1, resources.get(i).getResourceName());
            }
            System.out.println();
            libCounter++;
        }
    }

    private void bookResource() {
        boolean validLibrary = false;
        boolean validResource = false;
        boolean validBookingDate = false;
        boolean validBookingTime = false;
        Library selectedLibrary = null;
        Resource selectedResource = null;
        Booking selectedBooking = null;
        String bookingDate = null;
        int day = 0;
        int month = 0;
        int year = 0;

        while(!validLibrary) {
            showResources();
            ArrayList<Library> libraries = this.libSystem.getLibraries();
            System.out.printf("SELECT LIBRARY TO BORROW FROM (1 - %d): ", libraries.size());
            String library = keyboard.nextLine();

            try {
                int libraryIndex = Integer.parseInt(library) - 1;
                if (libraryIndex >= 0 && libraryIndex < libraries.size()) {
                    selectedLibrary = libraries.get(libraryIndex);
                    validLibrary = true;
                } else {
                    System.out.println("Invalid library. Please enter a number between 1 and " + libraries.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        while (!validResource) {
            ArrayList<Resource> resourceList = selectedLibrary.getResources();
            System.out.printf("SELECT RESOURCE TO BOOK (1 - %d): ", resourceList.size());
            String media = keyboard.nextLine();

            try {
                int resourceIndex = Integer.parseInt(media) - 1;

                if (resourceIndex >= 0 && resourceIndex < resourceList.size()) {
                    selectedResource = resourceList.get(resourceIndex);
                    validResource = true;
                } else {
                    System.out.println("Invalid resource selection. Please enter a number between 1 and " + (resourceList.size()));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        ResourceDisplay resourceDisplay = new ResourceDisplay(selectedResource);
        while(!validBookingDate) {
            System.out.printf("SELECT DATE TO BOOK (DD/MM/YY): "); // TODO: ensure proper preconditions
            bookingDate = keyboard.nextLine();

            try{
                if(bookingDate.length() == 8 &&
                        bookingDate.charAt(2) == '/' && bookingDate.charAt(5) == '/') {
                    day = Integer.parseInt(bookingDate.substring(0,2));
                    month = Integer.parseInt(bookingDate.substring(3,5));
                    year = Integer.parseInt(bookingDate.substring(6,8)) + 2000;
                    if(day >= 1 && day <= 31) {
                        if(month >= 1 && month <= 12) {
                            if(year > 2024) {
                                validBookingDate = true;
                            } else {
                                System.out.println("Year should be 2025 or later.");
                            }
                        } else {
                            System.out.println("Month should be between 1 and 12.");
                        }
                    } else {
                        System.out.println("Day should be between 1 and 31.");
                    }
                } else {
                    System.out.println("Invalid format. Try again");
                }
            } catch(NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }


        while(!validBookingTime) {
            System.out.println("SELECT TIME TO BOOK FROM 12:00 - 20:00 (HH:00-HH:00): ");
            resourceDisplay.printBookings(bookingDate);
            String bookingTime = keyboard.nextLine(); // HH:MM-HH:MM

            try {
                // Parse the booking time input
                if (bookingTime.length() == 11 &&
                        bookingTime.charAt(2) == ':' &&
                        bookingTime.charAt(5) == '-' &&
                        bookingTime.charAt(8) == ':') {
                    System.out.println("YA");
                    String startTime = bookingTime.substring(0, 5);
                    String endTime = bookingTime.substring(6, 11);
                    System.out.println("YE");
                    System.out.println("YI");

                    selectedBooking = new Booking(this.member, startTime, endTime, day, month, year);
                    if(selectedResource.validBookingFormat(selectedBooking)) {
                        if(selectedResource.validBookingTime(selectedBooking)) {
                            if(selectedResource.validBookingLimit(selectedBooking)) {
                                if(selectedResource.availableTimeSlot(selectedBooking)) {
                                    validBookingTime = true;
                                } else {
                                    System.out.println("This time slot has already been booked.");
                                }
                            } else {
                                System.out.println("You have already booked a resource for today.");
                            }
                        } else {
                            System.out.println("Your time slot must be 2 hours or less.");
                        }
                    } else {
                        System.out.println("This time slot is not available.");
                    }
                    System.out.println("YO");
                } else {
                    System.out.println("Invalid format. Use HH:MM-HH:MM (e.g., 14:00-16:00)");
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }

        boolean borrowed = this.member.bookResource(selectedResource, selectedBooking);
        if (borrowed) {
            System.out.println("Successfully booked: " + selectedResource.getResourceName() +
                    " at " + selectedResource.getBooking(member, bookingDate));
        } else {
            System.out.println("Failed to book: " + selectedResource.getResourceName() + ". No times available.");
        }

    }

    private void showMedia() {
        ArrayList<Library> libraries = this.libSystem.getLibraries();
        int libCounter = 1;
        for(Library library : libraries) {
            System.out.println(libCounter + ". " + library.getName());
            ArrayList<Media> media = library.getMedia();
            for(int i = 0; i < media.size(); i++) {
                System.out.printf("\t%d. \"%s\" by %s\n", i + 1, media.get(i).getTitle(), media.get(i).getAuthor());
            }
            System.out.println();
            libCounter++;
        }
    }

    private void borrowMedia() {
        boolean validLibrary = false;
        boolean validMedia = false;
        Library selectedLibrary = null;
        Media selectedMedia = null;

        while (!validLibrary) {
            showMedia();
            ArrayList<Library> libraries = this.libSystem.getLibraries();
            System.out.printf("SELECT LIBRARY TO BORROW FROM (1 - %d): ", libraries.size());
            String library = keyboard.nextLine();

            try {
                int libraryIndex = Integer.parseInt(library) - 1;
                if (libraryIndex >= 0 && libraryIndex < libraries.size()) {
                    selectedLibrary = libraries.get(libraryIndex);
                    validLibrary = true;
                } else {
                    System.out.println("Invalid library. Please enter a number between 1 and " + libraries.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        while (!validMedia) {
            ArrayList<Media> mediaList = selectedLibrary.getMedia();
            System.out.printf("SELECT MEDIA TO BORROW (1 - %d): ", mediaList.size());
            String media = keyboard.nextLine();

            try {
                int mediaIndex = Integer.parseInt(media) - 1;

                if (mediaIndex >= 0 && mediaIndex < mediaList.size()) {
                    selectedMedia = mediaList.get(mediaIndex);
                    validMedia = true;
                } else {
                    System.out.println("Invalid media selection. Please enter a number between 1 and " + (mediaList.size()));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        boolean borrowed = this.member.borrowMedia(selectedMedia);
        if (borrowed) {
            System.out.println("Successfully borrowed: " + selectedMedia.getTitle());
        } else {
            System.out.println("Failed to borrow: " + selectedMedia.getTitle() + ". No copies available.");
        }
    }

    private void returnMedia() {}

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
