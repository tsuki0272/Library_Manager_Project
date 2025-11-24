package ca.umanitoba.cs.longkuma.ui.member;

import ca.umanitoba.cs.longkuma.logic.exceptions.*;
import ca.umanitoba.cs.longkuma.logic.library.Library;
import ca.umanitoba.cs.longkuma.logic.library.LibrarySystem;
import ca.umanitoba.cs.longkuma.logic.media.Media;
import ca.umanitoba.cs.longkuma.logic.media.MediaCopy;
import ca.umanitoba.cs.longkuma.logic.media.Review;
import ca.umanitoba.cs.longkuma.logic.member.Member;
import ca.umanitoba.cs.longkuma.logic.resource.Resource;
import ca.umanitoba.cs.longkuma.ui.MediaCopyDisplay;
import ca.umanitoba.cs.longkuma.ui.ResourceDisplay;

import java.util.ArrayList;
import java.util.Scanner;

public class MemberActionsDisplay {
    private final LibrarySystem libSystem;
    private final Member member;
    private final Scanner keyboard;
    private static final String[] memberOptions = {"1. BOOK RESOURCE", "2. BORROW MEDIA", "3. RETURN MEDIA", "4. SIGN OUT"};

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
                case "1":
                case "BOOK RESOURCE":
                    System.out.println("You chose: BOOK RESOURCE");
                    bookResource();
                    break;
                case "2":
                case "BORROW MEDIA":
                    System.out.println("You chose: BORROW MEDIA");
                    if(this.member.hasConstraints()) {
                        System.out.println("Sorry, you have constraints that prevent you from borrowing more media.");
                    } else {
                        borrowMedia();
                    }
                    break;
                case "3":
                case "RETURN MEDIA":
                    System.out.println("You chose: RETURN MEDIA");
                    returnMedia();
                    break;
                case "4":
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

    private void showResources(Library library) {
        ArrayList<Resource> resources = library.getResources();
        for(int i = 0; i < resources.size(); i++) {
            System.out.printf("%d. \"%s\"\n", i + 1, resources.get(i).getResourceName());
        }
    }

    private void showLibraries() {
        ArrayList<Library> libraries = this.libSystem.getLibraries();
        for(int i = 0; i < libraries.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, libraries.get(i).getName());
        }
    }

    private Library selectLibrary() {
        Library selectedLibrary = null;
        boolean valid = false;

        while (!valid) {
            showLibraries();
            ArrayList<Library> libraries = this.libSystem.getLibraries();
            System.out.printf("SELECT LIBRARY (1 - %d): ", libraries.size());
            String input = getInput();

            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < libraries.size()) {
                    selectedLibrary = libraries.get(index);
                    valid = true;
                } else {
                    System.out.println("Invalid library. Please enter a number between 1 and " + libraries.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return selectedLibrary;
    }

    private Resource selectResource(Library library) {
        Resource selectedResource = null;
        boolean valid = false;

        while (!valid) {
            showResources(library);
            ArrayList<Resource> resources = library.getResources();
            System.out.printf("SELECT RESOURCE TO BOOK (1 - %d): ", resources.size());
            String input = getInput();

            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < resources.size()) {
                    selectedResource = resources.get(index);
                    valid = true;
                } else {
                    System.out.println("Invalid resource. Please enter a number between 1 and " + resources.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return selectedResource;
    }

    private Media selectMedia(Library library) {
        Media selectedMedia = null;
        boolean valid = false;

        while (!valid) {
            showMedia(library);
            ArrayList<Media> mediaList = library.getMedia();
            System.out.printf("SELECT MEDIA TO BORROW (1 - %d): ", mediaList.size());
            String input = getInput();

            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < mediaList.size()) {
                    selectedMedia = mediaList.get(index);
                    valid = true;
                } else {
                    System.out.println("Invalid media. Please enter a number between 1 and " + mediaList.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return selectedMedia;
    }

    private void bookResource() {
        Library selectedLibrary = selectLibrary();
        Resource selectedResource = selectResource(selectedLibrary);

        ResourceDisplay resourceDisplay = new ResourceDisplay(selectedResource);

        // Get date
        System.out.print("SELECT DATE TO BOOK (DD/MM/YY): ");
        String bookingDate = keyboard.nextLine();

        // Get time
        System.out.println("SELECT TIME TO BOOK FROM " + selectedResource.getOpeningTime() +
                " - " + selectedResource.getClosingTime() +
                " (format: HH:MM-HH:MM, timeslots every " +
                selectedResource.getTimeslotLength() + " minutes): ");
        resourceDisplay.printBookings(bookingDate);
        String bookingTime = keyboard.nextLine();

        try {
            boolean booked = member.bookResource(selectedResource, bookingDate, bookingTime);
            if (booked) {
                System.out.println("Successfully booked: " + selectedResource.getResourceName());
                System.out.println("Path to resource:");
                selectedLibrary.getMap().findResource(selectedResource);
            } else {
                System.out.println("Failed to book resource.");
            }
        } catch (InvalidDateException e) {
            System.out.println("Invalid date: " + e.getMessage());
        } catch (InvalidTimeFormatException e) {
            System.out.println("Invalid time format: " + e.getMessage());
        } catch (InvalidBookingFormatException e) {
            System.out.println("Invalid booking: " + e.getMessage());
        } catch (InvalidBookingDurationException e) {
            System.out.println("Booking duration error: " + e.getMessage());
        } catch (BookingLimitExceededException e) {
            System.out.println("Booking limit exceeded: " + e.getMessage());
        } catch (TimeSlotUnavailableException e) {
            System.out.println("Time slot unavailable: " + e.getMessage());
        } catch (InvalidMemberException e) {
            System.out.println("Invalid Member: " + e.getMessage());
        }
    }

    private void showMedia(Library library) {
        ArrayList<Media> media = library.getMedia();
        for(int i = 0; i < media.size(); i++) {
            System.out.printf("%d. \"%s\" by %s\n", i + 1, media.get(i).getTitle(), media.get(i).getAuthor());
        }
    }

    private void borrowMedia() {
        Library selectedLibrary = selectLibrary();
        Media selectedMedia = selectMedia(selectedLibrary);

        boolean borrowed = this.member.borrowMedia(selectedMedia);
        if (borrowed) {
            System.out.println("Path to selected media: ");
            selectedLibrary.getMap().findMedia(selectedMedia);
            System.out.println("Successfully borrowed: " + selectedMedia.getTitle());
        } else {
            System.out.println("Failed to borrow: " + selectedMedia.getTitle() + ". No copies available or you are on the waitlist.");
        }
    }

    private void returnMedia() {
        ArrayList<MediaCopy> borrowed = member.getBorrowedMedia();

        if (borrowed.isEmpty()) {
            System.out.println("You have no media to return.");
            return;
        }

        showBorrowedMedia(member);

        MediaCopy selectedCopy = null;
        Media selectedMedia = null;
        boolean validSelection = false;

        while (!validSelection) {
            System.out.printf("SELECT MEDIA TO RETURN (1 - %d): ", borrowed.size());
            String input = getInput();

            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < borrowed.size()) {
                    selectedCopy = borrowed.get(index);
                    selectedMedia = selectedCopy.getMedia();
                    validSelection = true;
                } else {
                    System.out.println("Invalid selection. Please enter a number between 1 and " + borrowed.size());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        boolean done = false;
        while (!done) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. READ REVIEWS");
            System.out.println("2. WRITE REVIEW");
            System.out.println("3. RETURN MEDIA");
            System.out.println("4. CANCEL");

            String action = getInput();

            switch (action) {
                case "1":
                case "READ REVIEWS":
                    readReviews(selectedMedia);
                    break;
                case "2":
                case "WRITE REVIEW":
                    writeReview(selectedMedia);
                    break;
                case "3":
                case "RETURN MEDIA":
                    boolean returned = member.returnMedia(selectedCopy);
                    if (returned) {
                        System.out.println("Successfully returned: " + selectedMedia.getTitle());
                        done = true;
                    } else {
                        System.out.println("Failed to return media.");
                    }
                    break;
                case "4":
                case "CANCEL":
                    System.out.println("Return cancelled.");
                    done = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private void readReviews(Media media) {
        ArrayList<Review> reviews = media.getReviews();
        if (reviews.isEmpty()) {
            System.out.println("No reviews yet for this media.");
        } else {
            System.out.println("=== Reviews for " + media.getTitle() + " ===");
            for (int i = 0; i < reviews.size(); i++) {
                System.out.println((i + 1) + ". " + reviews.get(i).getReview());
                System.out.println("--------------------");
            }
        }
    }

    private void writeReview(Media media) {
        System.out.println("Write your review for \"" + media.getTitle() + "\":");
        System.out.println("(Press Enter when done)");
        String reviewText = keyboard.nextLine();

        try {
            Review review = new Review.ReviewBuilder().review(reviewText).build();
            boolean added = media.addReview(review);
            if (added) {
                System.out.println("Review added successfully!");
            } else {
                System.out.println("Failed to add review.");
            }
        } catch (Exception e) {
            System.out.println("Error adding review: " + e.getMessage());
        }
    }

    private void showBorrowedMedia(Member member) {
        ArrayList<MediaCopy> borrowed = member.getBorrowedMedia();
        for(int i = 0; i < borrowed.size(); i++) {
            MediaCopyDisplay copyDisplay = new MediaCopyDisplay(borrowed.get(i));
            System.out.print((i + 1) + ". ");
            copyDisplay.print();
        }
    }

    private static void printOptions() {
        for (String memberOption : memberOptions) {
            System.out.println(memberOption);
        }
    }

    private String getInput() {
        return keyboard.nextLine().toUpperCase();
    }
}