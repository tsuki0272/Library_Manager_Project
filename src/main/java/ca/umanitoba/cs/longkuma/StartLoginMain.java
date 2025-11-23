package ca.umanitoba.cs.longkuma;

import ca.umanitoba.cs.longkuma.logic.library.Library;
import ca.umanitoba.cs.longkuma.logic.library.LibrarySystem;
import ca.umanitoba.cs.longkuma.logic.library.Map;
import ca.umanitoba.cs.longkuma.logic.media.Media;
import ca.umanitoba.cs.longkuma.logic.media.MediaCopy;
import ca.umanitoba.cs.longkuma.logic.resource.Resource;
import ca.umanitoba.cs.longkuma.ui.LoginDisplay;

public class StartLoginMain {

    private static LibrarySystem libSystem;

    public static void main(String[] args) {
        SetUpLibrarySystem();

        LoginDisplay display = new LoginDisplay();
        display.startLogin(libSystem);
    }

    private static void SetUpLibrarySystem() {
        try {
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

            Media media1 = new Media.MediaBuilder().title("The Hobbit").author("J.R.R. Tolkien").type("Book").build();
            MediaCopy copy1 = new MediaCopy.MediaCopyBuilder().copyNumber(1).media(media1).build();
            media1.addCopy(copy1);
            Media media2 = new Media.MediaBuilder().title("Thriller").author("Michael Jackson").type("CD").build();
            MediaCopy copy2 = new MediaCopy.MediaCopyBuilder().copyNumber(1).media(media2).build();
            media2.addCopy(copy2);
            lib1.addMedia(media1);
            lib1.addMedia(media2);

            Resource resource1 = new Resource.ResourceBuilder().resourceName("Individual Study Room").build();
            Resource resource2 = new Resource.ResourceBuilder().resourceName("Group Study Room").build();
            lib1.addResource(resource1);
            lib1.addResource(resource2);

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

            Media media3 = new Media.MediaBuilder().title("The Hunger Games").author("Suzanne Collins").type("Book").build();
            MediaCopy copy3 = new MediaCopy.MediaCopyBuilder().copyNumber(1).media(media3).build();
            media3.addCopy(copy3);
            Media media4 = new Media.MediaBuilder().title("Twilight").author("Stephenie Meyer").type("Book").build();
            MediaCopy copy4 = new MediaCopy.MediaCopyBuilder().copyNumber(1).media(media4).build();
            media4.addCopy(copy4);
            lib2.addMedia(media3);
            lib2.addMedia(media4);

            Resource resource3 = new Resource.ResourceBuilder().resourceName("Quiet Pod 1").build();
            Resource resource4 = new Resource.ResourceBuilder().resourceName("Quiet Pod 2").build();
            lib2.addResource(resource3);
            lib2.addResource(resource4);

            libSystem.addLibrary(lib1);
            libSystem.addLibrary(lib2);
        } catch (Exception e) {

        }
    }
}
