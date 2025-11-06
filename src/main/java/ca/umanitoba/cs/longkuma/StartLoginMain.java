package ca.umanitoba.cs.longkuma;

import ca.umanitoba.cs.longkuma.domain_model.library.LibrarySystem;
import ca.umanitoba.cs.longkuma.ui.LoginDisplay;

public class StartLoginMain {

    private static LibrarySystem libSystem;

    public static void main(String args[]) {
        SetUpLibrarySystem();

        LoginDisplay display = new LoginDisplay();
        display.startLogin();
    }

    private static void SetUpLibrarySystem() {
        libSystem = new LibrarySystem();
    }
}
