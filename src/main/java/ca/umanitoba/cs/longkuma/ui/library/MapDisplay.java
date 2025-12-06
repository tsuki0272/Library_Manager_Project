package ca.umanitoba.cs.longkuma.ui.library;

import ca.umanitoba.cs.longkuma.domain.library.Map;
import ca.umanitoba.cs.longkuma.domain.media.Media;
import ca.umanitoba.cs.longkuma.domain.resource.Resource;
import ca.umanitoba.cs.longkuma.logic.stack.Pathfinding;
import java.util.List;

public class MapDisplay {
    private final Map map;

    /*
     * Constructs a MapDisplay with the specified map
     *
     * @param map The library map to display
     */
    public MapDisplay(Map map) {
        this.map = map;
    }

    /*
     * Displays the path from kiosk to a media item on the map
     * Shows error messages if media is not in map or no path exists
     *
     * @param media The media item to display the path to
     */
    public void displayPathToMedia(Media media) {
        List<int[]> path = Pathfinding.findMediaPath(map, media);

        if (path == null) {
            System.out.println("This media is not in the map!");
        } else if (path.isEmpty()) {
            System.out.println("There's no way to get to this media!");
        } else {
            System.out.println("Path to media:");
            displayPath(path);
        }
    }

    /*
     * Displays the path from kiosk to a resource on the map
     * Shows error messages if resource is not in map or no path exists
     *
     * @param resource The resource to display the path to
     */
    public void displayPathToResource(Resource resource) {
        List<int[]> path = Pathfinding.findResourcePath(map, resource);

        if (path == null) {
            System.out.println("This resource is not in the map!");
        } else if (path.isEmpty()) {
            System.out.println("There's no way to get to this resource!");
        } else {
            System.out.println("Path to resource:");
            displayPath(path);
        }
    }

    /*
     * Displays a path on the map grid
     * Marks intermediate path points with asterisks, preserves kiosk and destination markers
     *
     * @param path The list of coordinate arrays representing the path
     */
    private void displayPath(List<int[]> path) {
        if (path != null && !path.isEmpty()) {
            char[][] grid = map.getGrid();
            int[] kioskCoords = map.getKioskCoordinates();
            int[] destination = path.get(path.size() - 1);

            char[][] gridCopy = deepCopy(grid);

            for (int i = 1; i < path.size() - 1; i++) {
                int[] coord = path.get(i);
                gridCopy[coord[0]][coord[1]] = '*';
            }

            System.out.println(formatGrid(gridCopy, kioskCoords, destination));
        }
    }

    /*
     * Formats the map grid as a string with special markers for kiosk and destination
     * Converts the 2D grid to a readable string representation with spacing
     *
     * @param grid The map grid to format
     * @param kioskCoords The coordinates of the kiosk (marked as 'U')
     * @param destination The coordinates of the destination (marked as 'X')
     * @return A formatted string representation of the grid
     */
    private String formatGrid(char[][] grid, int[] kioskCoords, int[] destination) {
        StringBuilder output = new StringBuilder();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (row == kioskCoords[0] && col == kioskCoords[1]) output.append("U ");
                else if (row == destination[0] && col == destination[1]) output.append("X ");
                else output.append(grid[row][col]).append(" ");
            }
            output.append("\n");
        }
        return output.toString();
    }

    /*
     * Displays the map legend showing symbol meanings
     * Includes standard legend entries plus path markers
     */
    public void displayLegend() {
        String[] legend = map.getLegend();
        System.out.println("=== Map Legend ===");
        for (String entry : legend) System.out.println(entry);
        System.out.println("U = You are here (Kiosk)");
        System.out.println("X = Destination");
        System.out.println("* = Path");
        System.out.println("==================");
    }

    /*
     * Creates a deep copy of a 2D character array
     * Each row is cloned independently to prevent shared references
     *
     * @param original The original 2D character array to copy
     * @return A new 2D character array with copied values
     */
    private char[][] deepCopy(char[][] original) {
        char[][] copy = new char[original.length][];
        for (int i = 0; i < original.length; i++) copy[i] = original[i].clone();
        return copy;
    }
}