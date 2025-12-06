package ca.umanitoba.cs.longkuma.ui.member;

import ca.umanitoba.cs.longkuma.domain.exceptions.*;
import ca.umanitoba.cs.longkuma.domain.media.Media;
import ca.umanitoba.cs.longkuma.domain.media.MediaCopy;
import ca.umanitoba.cs.longkuma.domain.member.Member;
import ca.umanitoba.cs.longkuma.domain.library.Library;
import ca.umanitoba.cs.longkuma.domain.resource.Resource;
import ca.umanitoba.cs.longkuma.logic.library.LibrarySystem;
import ca.umanitoba.cs.longkuma.domain.media.Review;
import ca.umanitoba.cs.longkuma.logic.media.MediaBorrowingLogic;
import ca.umanitoba.cs.longkuma.ui.library.MapDisplay;
import ca.umanitoba.cs.longkuma.ui.media.MediaCopyDisplay;
import ca.umanitoba.cs.longkuma.ui.resource.ResourceDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MemberActionsDisplay {
    private final LibrarySystem libSystem;
    private final Member member;
    private final Scanner keyboard;
    private static final String[] memberOptions = {"1. BOOK RESOURCE", "2. BORROW MEDIA", "3. RETURN MEDIA", "4. SIGN OUT"};

    /**
     * Constructs a MemberActionsDisplay with the specified library system, member, and scanner
     *
     * @param libSystem The library system to interact with
     * @param member The currently logged-in member
     * @param keyboard The scanner for user input
     */
    public MemberActionsDisplay(LibrarySystem libSystem, Member member, Scanner keyboard) {
        this.libSystem = libSystem;
        this.member = member;
        this.keyboard = keyboard;
    }

    /**
     * Displays the main menu options and handles member actions
     * Continues until the member chooses to sign out
     */
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
                    System.out.println("Invalid option. Please enter a number between 1 and 4.");
                    break;
            }
        }
    }

    /**
     * Displays all resources available in a library
     *
     * @param library The library whose resources to display
     */
    private void showResources(Library library) {
        ArrayList<Resource> resources = library.getResources();
        for(int i = 0; i < resources.size(); i++) {
            System.out.printf("%d. \"%s\"\n", i + 1, resources.get(i).getResourceName());
        }
    }

    /**
     * Displays all libraries in the system
     */
    private void showLibraries() {
        ArrayList<Library> libraries = this.libSystem.getLibraries();
        for(int i = 0; i < libraries.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, libraries.get(i).getName());
        }
    }

    /**
     * Prompts the user to select a library from available options
     *
     * @return The selected library
     */
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

    /**
     * Prompts the user to select a resource from a library
     *
     * @param library The library containing the resources
     * @return The selected resource
     */
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

    /**
     * Prompts the user to select media from a library
     *
     * @param library The library containing the media
     * @return The selected media item
     */
    private Media selectMedia(Library library) {
        Media selectedMedia = null;
        boolean valid = false;

        while (!valid) {
            showMedia(library);
            List<Media> mediaList = library.getMedia();
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

    /**
     * Handles the resource booking process including:
     * - Library and resource selection
     * - Date and time input
     * - Path display to the resource
     * - Exception handling for booking errors
     * Returns to main menu after 3 invalid date attempts
     */
    private void bookResource() {
        Library selectedLibrary = selectLibrary();
        Resource selectedResource = selectResource(selectedLibrary);

        ResourceDisplay resourceDisplay = new ResourceDisplay(selectedResource);

        final int MAX_ATTEMPTS = 3;
        int dateAttempts = 0;
        boolean validDate = false;
        String bookingDate = "";

        // Get date with retry limit
        while (!validDate && dateAttempts < MAX_ATTEMPTS) {
            System.out.print("SELECT DATE TO BOOK (DD/MM/YY): ");
            bookingDate = keyboard.nextLine();
            dateAttempts++;

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
                    System.out.println("Path to selected resource: ");
                    MapDisplay mapDisplay = new MapDisplay(selectedLibrary.getMap());
                    mapDisplay.displayPathToResource(selectedResource);
                    mapDisplay.displayLegend();
                    System.out.println("Successfully booked: " + selectedResource.getResourceName());
                    validDate = true;
                } else {
                    System.out.println("Failed to book resource.");
                }
            } catch (InvalidDateException e) {
                System.out.println("Invalid date: " + e.getMessage());
                if (dateAttempts < MAX_ATTEMPTS) {
                    System.out.println("Please try again. Attempt " + dateAttempts + " of " + MAX_ATTEMPTS);
                } else {
                    System.out.println("Maximum attempts reached. Returning to main menu.");
                }
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
    }

    /**
     * Displays all media available in a library
     *
     * @param library The library whose media to display
     */
    private void showMedia(Library library) {
        List<Media> media = library.getMedia();
        for(int i = 0; i < media.size(); i++) {
            System.out.printf("%d. \"%s\" by %s\n", i + 1, media.get(i).getTitle(), media.get(i).getAuthor());
        }
    }

    /**
     * Handles the media borrowing process including:
     * - Library and media selection
     * - Path display to the media
     * - Waitlist handling if no copies available
     */
    private void borrowMedia() {
        Library selectedLibrary = selectLibrary();
        Media selectedMedia = selectMedia(selectedLibrary);

        MediaBorrowingLogic.BorrowResult result =
                MediaBorrowingLogic.getInstance()
                        .borrow(selectedMedia, member, "12:00", "01/01/30");

        if (result.getStatus() == MediaBorrowingLogic.BorrowResult.Status.SUCCESS) {
            System.out.println("Path to selected media: ");
            MapDisplay mapDisplay = new MapDisplay(selectedLibrary.getMap());
            mapDisplay.displayPathToMedia(selectedMedia);
            mapDisplay.displayLegend();
            System.out.println("Successfully borrowed: " + selectedMedia.getTitle());
        } else if (result.getStatus() == MediaBorrowingLogic.BorrowResult.Status.WAITLISTED) {
            System.out.println("Added to waitlist for: " + selectedMedia.getTitle());
        } else {
            System.out.println("Failed to borrow media.");
        }
    }


    /**
     * Handles the media return process including:
     * - Display of borrowed media
     * - Media selection for return
     * - Review reading/writing options
     * - Actual return of media copy
     */
    private void returnMedia() {
        List<MediaCopy> borrowed = member.getBorrowedMedia();

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
                    boolean returned =
                            MediaBorrowingLogic.getInstance().returnCopy(selectedCopy, member)
                                    .getStatus() == MediaBorrowingLogic.BorrowResult.Status.SUCCESS;

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

    /**
     * Displays all reviews for a media item
     *
     * @param media The media item whose reviews to display
     */
    private void readReviews(Media media) {
        List<Review> reviews = media.getReviews();
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

    /**
     * Handles writing a review for a media item
     *
     * @param media The media item to review
     */
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

    /**
     * Displays all media currently borrowed by the member
     *
     * @param member The member whose borrowed media to display
     */
    private void showBorrowedMedia(Member member) {
        List<MediaCopy> borrowed = member.getBorrowedMedia();
        for(int i = 0; i < borrowed.size(); i++) {
            MediaCopyDisplay copyDisplay = new MediaCopyDisplay(borrowed.get(i));
            System.out.print((i + 1) + ". ");
            copyDisplay.print();
        }
    }

    /**
     * Prints the available member options to the console
     */
    private static void printOptions() {
        for (String memberOption : memberOptions) {
            System.out.println(memberOption);
        }
    }

    /**
     * Gets user input from the keyboard and converts to uppercase
     *
     * @return The user input in uppercase
     */
    private String getInput() {
        return keyboard.nextLine().toUpperCase();
    }
}