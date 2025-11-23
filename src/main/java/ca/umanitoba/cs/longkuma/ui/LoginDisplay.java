package ca.umanitoba.cs.longkuma.ui;

import ca.umanitoba.cs.longkuma.logic.library.LibrarySystem;
import ca.umanitoba.cs.longkuma.logic.member.Member;
import ca.umanitoba.cs.longkuma.ui.member.MemberActionsDisplay;

import java.util.Scanner;

public class LoginDisplay {

    private final Scanner keyboard;
    private static final String[] loginOptions = {"EXISTING ACCOUNT", "NEW ACCOUNT"};
    private boolean downForMaintenance;

    public LoginDisplay() {
        this.keyboard = new Scanner(System.in);
        this.downForMaintenance = false;
    }

    public void startLogin(LibrarySystem libSystem) {
        String task;
        System.out.print("WELCOME TO THE SD CITY LIBRARY SYSTEM. ");
        while(!downForMaintenance) {
            System.out.println("SELECT AN OPTION: ");
            printOptions();
            task = getInput();

            switch(task) {
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
                    System.out.println("I don't know what that means.");
                    break;
            }
        }
    }

    private void getMemberActions(LibrarySystem libSystem, Member currMember) {
        MemberActionsDisplay memberActions = new MemberActionsDisplay(libSystem, currMember, keyboard);
        memberActions.showOptions();
    }

    private void createNewAccount(LibrarySystem libSystem) {
        boolean validCredentials = false;
        while(!validCredentials) {

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
            } else if(password.equals(passwordValidator)) {
                if(libSystem.showMember(name) == null) {
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

    private Member login(LibrarySystem libSystem) {
        boolean validCredentials = false;
        Member currMember = null;
        while(!validCredentials) {

            System.out.print("Enter full name: ");
            String name = keyboard.nextLine();

            System.out.print("Enter password: ");
            String password = keyboard.nextLine();

            currMember = libSystem.showMember(name);
            if(currMember != null && currMember.getPassword().equals(password)) {
                System.out.println("Signed in successfully!");
                System.out.println();
                validCredentials = true;
            } else {
                System.out.println("Account and password do not match. Try again.");
            }
        }
        return currMember;
    }

    private String getInput() {
        String loginInput;
        loginInput = keyboard.nextLine().toUpperCase();
        return loginInput;
    }

    private static void printOptions() {
        for(int i = 0; i < loginOptions.length; i++) {
            System.out.printf("%s \n", loginOptions[i]);
        }
    }

}
