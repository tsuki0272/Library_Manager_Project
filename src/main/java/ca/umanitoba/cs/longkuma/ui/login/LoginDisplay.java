package ca.umanitoba.cs.longkuma.ui.login;

import ca.umanitoba.cs.longkuma.domain.member.Member;
import ca.umanitoba.cs.longkuma.logic.library.LibrarySystem;
import ca.umanitoba.cs.longkuma.ui.member.MemberActionsDisplay;

import java.util.Scanner;

public class LoginDisplay {

    private final Scanner keyboard;
    private static final String[] loginOptions = {"EXISTING ACCOUNT", "NEW ACCOUNT"};
    private final boolean downForMaintenance;

    /**
     * Constructs a LoginDisplay with a new Scanner for user input
     */
    public LoginDisplay() {
        this.keyboard = new Scanner(System.in);
        this.downForMaintenance = false;
    }

    /**
     * Starts the login process and displays the main login menu
     *
     * @param libSystem The library system to interact with
     */
    public void startLogin(LibrarySystem libSystem) {
        String task;
        System.out.print("WELCOME TO THE SD CITY LIBRARY SYSTEM. ");
        while (!downForMaintenance) {
            System.out.println("SELECT AN OPTION: ");
            printOptions();
            task = getInput();

            switch (task) {
                case "1":
                case "EXISTING ACCOUNT":
                    System.out.println("You chose: EXISTING ACCOUNT");
                    Member currMember = login(libSystem);
                    getMemberActions(libSystem, currMember);
                    break;
                case "2":
                case "NEW ACCOUNT":
                    System.out.println("You chose: NEW ACCOUNT");
                    createNewAccount(libSystem);
                    break;
                default:
                    System.out.println("Invalid option. Please enter '1' for EXISTING ACCOUNT or '2' for NEW ACCOUNT.");
                    break;
            }
        }
    }

    /**
     * Transfers control to member actions after successful login
     *
     * @param libSystem  The library system to interact with
     * @param currMember The currently logged-in member
     */
    private void getMemberActions(LibrarySystem libSystem, Member currMember) {
        MemberActionsDisplay memberActions = new MemberActionsDisplay(libSystem, currMember, keyboard);
        memberActions.showOptions();
    }

    /**
     * Handles the creation of a new member account
     *
     * @param libSystem The library system to add the new member to
     */
    private void createNewAccount(LibrarySystem libSystem) {
        boolean validCredentials = false;
        while (!validCredentials) {

            System.out.print("Enter full name: ");
            String name = keyboard.nextLine();

            System.out.print("Enter password: ");
            String password = keyboard.nextLine();

            System.out.print("Confirm password: ");
            String passwordValidator = keyboard.nextLine();

            if (name.isEmpty()) {
                System.out.println("Error: Name cannot be empty. Please try again.\n");
                continue;
            } else if (password.isEmpty()) {
                System.out.println("Error: Password cannot be empty. Please try again.\n");
                continue;
            } else if (password.equals(passwordValidator)) {
                if (libSystem.showMember(name) == null) {
                    System.out.println("Account created successfully! Returning to login...");
                    System.out.println();
                    try {
                        Member newMember = new Member.MemberBuilder().name(name).password(password).build();
                        libSystem.addMember(newMember);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    validCredentials = true;
                } else {
                    System.out.println("You are already registered. Try signing in instead.");
                }

            } else {
                System.out.println("Passwords do not match. Try again.");
            }
        }
    }

    /**
     * Handles the login process for existing members
     *
     * @param libSystem The library system to validate credentials against
     * @return The logged-in member if credentials are valid, null otherwise
     */
    private Member login(LibrarySystem libSystem) {
        boolean validCredentials = false;
        Member currMember = null;
        while (!validCredentials) {

            System.out.print("Enter full name: ");
            String name = keyboard.nextLine();

            System.out.print("Enter password: ");
            String password = keyboard.nextLine();

            currMember = libSystem.showMember(name);
            if (currMember != null && currMember.getPassword().equals(password)) {
                System.out.println("Signed in successfully!");
                System.out.println();
                validCredentials = true;
            } else {
                System.out.println("Account and password do not match. Try again.");
            }
        }
        return currMember;
    }

    /**
     * Gets user input from the keyboard and converts to uppercase
     *
     * @return The user input in uppercase
     */
    private String getInput() {
        String loginInput;
        loginInput = keyboard.nextLine().toUpperCase();
        return loginInput;
    }

    /**
     * Prints the available login options to the console
     */
    private static void printOptions() {
        for (int i = 0; i < loginOptions.length; i++) {
            System.out.printf("%s \n", loginOptions[i]);
        }
    }

}