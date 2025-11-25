package ca.umanitoba.cs.longkuma.ui.login;

import ca.umanitoba.cs.longkuma.logic.library.Library;
import ca.umanitoba.cs.longkuma.logic.library.LibrarySystem;
import ca.umanitoba.cs.longkuma.logic.library.Map;
import ca.umanitoba.cs.longkuma.logic.media.Media;
import ca.umanitoba.cs.longkuma.logic.media.MediaCopy;
import ca.umanitoba.cs.longkuma.logic.resource.Resource;

import java.util.ArrayList;

public class StartLoginMain {

    private static LibrarySystem libSystem;

    /**
     * Main entry point for the library system application
     * Sets up the library system and starts the login display
     */
    public static void main(String[] args) {
        SetUpLibrarySystem();

        LoginDisplay display = new LoginDisplay();
        display.startLogin(libSystem);
    }

    /**
     * Initializes the library system with sample data including:
     * - Two libraries with maps
     * - Media items with copies
     * - Study room resources
     * - Coordinates for all items on the maps
     */
    private static void SetUpLibrarySystem() {
        try {
            libSystem = new LibrarySystem.LibrarySystemBuilder().build();

            String mapString1 = """
                    9 12
                    W W W W W W W W W W W W
                    W . . . . . . . G G G W
                    W . M F . . . . G G G W
                    W . . . . . . . . . . W
                    W . . W W W W W W . I W
                    W . . W . U . . W . . W
                    W . . W . . . . W . . W
                    W . . . . . . . . . . W
                    W W W W W W W W W W W W""";
            String[] legend1 = {
                    "W,Wall",
                    ".,Path/Walking Space",
                    "M,Music",
                    "F,Fiction",
                    "I,Individual Study Room",
                    "G,Group Study Room",
                    "U,You are here"
            };
            int[] kiosk1 = new int[]{5,5};
            char[][] mapGrid1 = Map.MapBuilder.gridFromString(mapString1);
            Map map1 = new Map.MapBuilder().grid(mapGrid1).legend(legend1).kioskCoordinates(kiosk1).build();
            Library lib1 = new Library.LibraryBuilder().name("Elizabeth Dafoe Library").map(map1).build();

            Media media1 = new Media.MediaBuilder().title("The Hobbit").author("J.R.R. Tolkien")
                    .type("Book").coordinates(new int[]{2,3}).build();
            MediaCopy copy1 = new MediaCopy.MediaCopyBuilder().copyNumber(1).media(media1).build();
            media1.addCopy(copy1);

            Media media2 = new Media.MediaBuilder().title("Thriller").author("Michael Jackson")
                    .type("CD").coordinates(new int[]{2,2}).build();
            MediaCopy copy2 = new MediaCopy.MediaCopyBuilder().copyNumber(1).media(media2).build();
            media2.addCopy(copy2);

            lib1.addMedia(media1);
            lib1.addMedia(media2);

            final String OPENING_TIME = "12:00";
            final String CLOSING_TIME = "20:00";
            final int BOOKING_TIMESLOTS = 60;

            ArrayList<int[]> individualStudyRoomCoords = new ArrayList<>();
            individualStudyRoomCoords.add(new int[]{4,10});
            Resource resource1 = new Resource.ResourceBuilder().resourceName("Individual Study Room").openingTime(OPENING_TIME)
                    .closingTime(CLOSING_TIME).timeslotLength(BOOKING_TIMESLOTS).coordinates(individualStudyRoomCoords).build();

            ArrayList<int[]> groupStudyRoomCoords = new ArrayList<>();
            groupStudyRoomCoords.add(new int[]{1,8});
            groupStudyRoomCoords.add(new int[]{2,8});
            groupStudyRoomCoords.add(new int[]{1,9});
            groupStudyRoomCoords.add(new int[]{2,9});
            groupStudyRoomCoords.add(new int[]{1,10});
            groupStudyRoomCoords.add(new int[]{2,10});
            Resource resource2 = new Resource.ResourceBuilder().resourceName("Group Study Room").openingTime(OPENING_TIME)
                    .closingTime(CLOSING_TIME).timeslotLength(BOOKING_TIMESLOTS).coordinates(groupStudyRoomCoords).build();
            lib1.addResource(resource1);
            lib1.addResource(resource2);

            map1.addMediaCoordinates(media1.getCoordinates());
            map1.addMediaCoordinates(media2.getCoordinates());
            map1.addResourceCoordinates(individualStudyRoomCoords);
            map1.addResourceCoordinates(groupStudyRoomCoords);
            // ======== LIBRARY 2 =========

            String mapString2 = """
                    7 12
                    W W W W W W W W W W W W
                    W . . . . . . . . . . W
                    W . S . . . . F . . . W
                    W . . . . W W W W W . W
                    W . 1 2 . W . U . . . W
                    W . . . . W . . . . . W
                    W W W W W W W W W W W W""";
            String[] legend2 = {
                    "W,Wall",
                    ".,Path/Walking Space",
                    "S,Science",
                    "F,Fiction",
                    "1,Quiet Pod 1",
                    "2,Quiet Pod 2",
                    "U,You are here"
            };
            int[] kiosk2 = new int[]{4,7};
            char[][] mapGrid2 = Map.MapBuilder.gridFromString(mapString2);
            Map map2 = new Map.MapBuilder().grid(mapGrid2).legend(legend2).kioskCoordinates(kiosk2).build();
            Library lib2 = new Library.LibraryBuilder().name("E. K. Williams Law Library").map(map2).build();

            Media media3 = new Media.MediaBuilder().title("Generative Adversarial Networks").author("Ian J. Goodfellow")
                    .type("Scientific Paper").coordinates(new int[]{2,2}).build();
            MediaCopy copy3 = new MediaCopy.MediaCopyBuilder().copyNumber(1).media(media3).build();
            media3.addCopy(copy3);
            Media media4 = new Media.MediaBuilder().title("Twilight").author("Stephenie Meyer")
                    .type("Book").coordinates(new int[]{2,7}).build();
            MediaCopy copy4 = new MediaCopy.MediaCopyBuilder().copyNumber(1).media(media4).build();
            media4.addCopy(copy4);

            lib2.addMedia(media3);
            lib2.addMedia(media4);

            ArrayList<int[]> quietPod1Coords = new ArrayList<>();
            quietPod1Coords.add(new int[]{4,2});
            Resource resource3 = new Resource.ResourceBuilder().resourceName("Quiet Pod 1").openingTime(OPENING_TIME)
                    .closingTime(CLOSING_TIME).timeslotLength(BOOKING_TIMESLOTS).coordinates(quietPod1Coords).build();
            ArrayList<int[]> quietPod2Coords = new ArrayList<>();
            quietPod2Coords.add(new int[]{4,3});
            Resource resource4 = new Resource.ResourceBuilder().resourceName("Quiet Pod 2").openingTime(OPENING_TIME)
                    .closingTime(CLOSING_TIME).timeslotLength(BOOKING_TIMESLOTS).coordinates(quietPod2Coords).build();
            lib2.addResource(resource3);
            lib2.addResource(resource4);

            map2.addMediaCoordinates(media3.getCoordinates());
            map2.addMediaCoordinates(media4.getCoordinates());
            map2.addResourceCoordinates(quietPod1Coords);
            map2.addResourceCoordinates(quietPod2Coords);

            libSystem.addLibrary(lib1);
            libSystem.addLibrary(lib2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}