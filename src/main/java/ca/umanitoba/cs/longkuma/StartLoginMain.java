package ca.umanitoba.cs.longkuma;

import ca.umanitoba.cs.longkuma.domain_model.library.Library;
import ca.umanitoba.cs.longkuma.domain_model.library.LibrarySystem;
import ca.umanitoba.cs.longkuma.domain_model.library.Map;
import ca.umanitoba.cs.longkuma.domain_model.media.Media;
import ca.umanitoba.cs.longkuma.ui.LoginDisplay;
import jdk.jshell.spi.ExecutionControlProvider;

public class StartLoginMain {

    private static LibrarySystem libSystem;

    public static void main(String[] args) {
        SetUpLibrarySystem();

        LoginDisplay display = new LoginDisplay();
        display.startLogin(libSystem);
    }

    private static void SetUpLibrarySystem() {
        try{
            libSystem = new LibrarySystem.LibrarySystemBuilder().build();

            String mapString1 = """
            9 12
            W W W W W W W W W W W W
            W . . . . . . . . . . W
            W . S F M H . B C A R W
            W . . . . . . . . . . W
            W . . W W W W W W . . W
            W . . W . U . . W . . W
            W . . W . . . . W . . W
            W . . . . . . . . . . W
            W W W W W W W W W W W W""";
            String[] legend1 = {
                    "W,Wall",
                    ".,Path/Walking Space",
                    "S,Science",
                    "F,Fiction",
                    "M,Math",
                    "H,History",
                    "B,Biography",
                    "C,Computers",
                    "A,Arts",
                    "R,Religion",
                    "U,You are here"
            };
            char[][] mapGrid1 = Map.MapBuilder.gridFromString(mapString1);
            Map map1 = new Map.MapBuilder().grid(mapGrid1).legend(legend1).build();
            Library lib1 = new Library.LibraryBuilder().name("Elizabeth Dafoe Library").map(map1).build();
            Media media1 = new Media("The Hobbit", "J.R.R. Tolkien", "Book");
            Media media2 = new Media("Thriller", "Michael Jackson", "CD");
            lib1.addMedia(media1);
            lib1.addMedia(media2);

            String mapString2 = """
            7 12
            W W W W W W W W W W W W
            W . . . . . . . . . . W
            W . S F M H B C A R . W
            W . . . . W W W W W . W
            W . P L G W . U . . . W
            W . . . . W . . . . . W
            W W W W W W W W W W W W""";
            String[] legend2 = {
                    "W,Wall",
                    ".,Path/Walking Space",
                    "S,Science",
                    "F,Fiction",
                    "M,Math",
                    "H,History",
                    "B,Biography",
                    "C,Computers",
                    "A,Arts",
                    "R,Religion",
                    "P,Philosophy",
                    "L,Literature",
                    "G,Geography",
                    "U,You are here"
            };
            char[][] mapGrid2 = Map.MapBuilder.gridFromString(mapString2);
            Map map2 = new Map.MapBuilder().grid(mapGrid2).legend(legend2).build();
            Library lib2 = new Library.LibraryBuilder().name("E. K. Williams Law Library").map(map2).build();
            Media media3 = new Media("The Hunger Games", "Suzanne Collins", "Book");
            Media media4 = new Media("Twilight", "Stephenie Meyer", "Book");
            lib2.addMedia(media3);
            lib2.addMedia(media4);

            libSystem.addLibrary(lib1);
            libSystem.addLibrary(lib2);
        } catch(Exception e) {

        }
    }
}
