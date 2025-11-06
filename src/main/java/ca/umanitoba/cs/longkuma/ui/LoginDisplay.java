package ca.umanitoba.cs.longkuma.ui;

import ca.umanitoba.cs.longkuma.logic.Login;

import java.util.List;
import java.util.Scanner;

public class LoginDisplay {

    private final Login login;
    private Scanner keyboard;
    private static String[] loginOptions = {"EXISTING ACCOUNT", "NEW ACCOUNT"};
    private boolean downForMaintenance;

    public LoginDisplay() {
        this.login = new Login();
        this.keyboard = new Scanner(System.in);
        this.downForMaintenance = false;
    }

    public void startLogin() {
        String task;
        System.out.println("WELCOME TO THE SD CITY LIBRARY SYSTEM. SELECT AN OPTION: ");
        printOptions();
        while(!downForMaintenance) {
            task = getInput();

            switch(task) {
                case "EXISTING ACCOUNT":
                    login();
                    break;
                case "NEW ACCOUNT":
                    createNewAccount();
                    break;
            }
        }
    }

    private void createNewAccount() {
        boolean validCredentials = false;

    }

    private void login() {

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
