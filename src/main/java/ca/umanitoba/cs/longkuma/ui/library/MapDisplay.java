package ca.umanitoba.cs.longkuma.ui.library;

import ca.umanitoba.cs.longkuma.logic.library.Map;
import ca.umanitoba.cs.longkuma.logic.media.Media;
import ca.umanitoba.cs.longkuma.logic.resource.Resource;

import java.util.ArrayList;

public class MapDisplay {
    private final Map map;

    /**
     * Constructs a MapDisplay with the specified map
     *
     * @param map The map to display
     */
    public MapDisplay(Map map) {
        this.map = map;
    }

    /**
     * Displays the path to the specified media item
     *
     * @param media The media item to find a path to
     */
    public void displayPathToMedia(Media media) {
        ArrayList<int[]> path = map.findMedia(media);

        if (path == null) {
            System.out.println("This media is not in the map!");
        } else if (path.isEmpty()) {
            System.out.println("There's no way to get to this media!");
        } else {
            System.out.println("Path to media:");
            displayPath(path);
        }
    }

    /**
     * Displays the path to the specified resource
     *
     * @param resource The resource to find a path to
     */
    public void displayPathToResource(Resource resource) {
        ArrayList<int[]> path = map.findResource(resource);

        if (path == null) {
            System.out.println("This resource is not in the map!");
        } else if (path.isEmpty()) {
            System.out.println("There's no way to get to this resource!");
        } else {
            System.out.println("Path to resource:");
            displayPath(path);
        }
    }

    /**
     * Displays a path on the map grid
     *
     * @param path The list of coordinates representing the path
     */
    private void displayPath(ArrayList<int[]> path) {
        if (path != null && !path.isEmpty()) {
            char[][] grid = map.getGrid();
            int[] kioskCoords = map.getKioskCoordinates();
            int[] destination = path.get(path.size() - 1);

            // Create copy to mark the path
            char[][] gridCopy = deepCopy(grid);

            // Mark the path (except start and end)
            for (int i = 1; i < path.size() - 1; i++) {
                int[] coord = path.get(i);
                gridCopy[coord[0]][coord[1]] = '*';
            }

            System.out.println(formatGrid(gridCopy, kioskCoords, destination));
        }
    }

    /**
     * Formats the grid with path markings for display
     * Marks kiosk with 'U' and destination with 'X'
     *
     * @param grid The 2D character array representing the map
     * @param kioskCoords The kiosk coordinates [row, column]
     * @param destination The destination coordinates [row, column]
     * @return String representation of the grid with markings
     */
    private String formatGrid(char[][] grid, int[] kioskCoords, int[] destination) {
        StringBuilder output = new StringBuilder();

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (row == kioskCoords[0] && col == kioskCoords[1]) {
                    output.append("U "); // Kiosk/starting point
                } else if (row == destination[0] && col == destination[1]) {
                    output.append("X "); // Destination
                } else {
                    output.append(grid[row][col]).append(" ");
                }
            }
            output.append("\n");
        }

        return output.toString();
    }

    /**
     * Displays the map legend with symbol explanations
     */
    public void displayLegend() {
        String[] legend = map.getLegend();
        System.out.println("=== Map Legend ===");
        for (String entry : legend) {
            System.out.println(entry);
        }
        System.out.println("U = You are here (Kiosk)");
        System.out.println("X = Destination");
        System.out.println("* = Path");
        System.out.println("==================");
    }

    /**
     * Displays the complete library map with kiosk location
     */
    public void displayMap() {
        char[][] grid = map.getGrid();
        int[] kioskCoords = map.getKioskCoordinates();

        System.out.println("=== Library Map ===");
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (row == kioskCoords[0] && col == kioskCoords[1]) {
                    System.out.print("U ");
                } else {
                    System.out.print(grid[row][col] + " ");
                }
            }
            System.out.println();
        }
        System.out.println("===================");
    }

    /**
     * Creates a deep copy of a 2D character array
     *
     * @param original Original 2D character array
     * @return Deep copy of the original array
     */
    private char[][] deepCopy(char[][] original) {
        char[][] copy = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
}