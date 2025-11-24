package ca.umanitoba.cs.longkuma.logic.library;

import ca.umanitoba.cs.longkuma.logic.media.Media;
import ca.umanitoba.cs.longkuma.logic.media.MediaCopy;
import ca.umanitoba.cs.longkuma.logic.resource.Resource;
import ca.umanitoba.cs.longkuma.logic.stack.LinkedListStack;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Scanner;

public class Map {
    private final char[][]  grid;
    private final String[] legend;
    private ArrayList<int[]> mediaCoordinates;
    private ArrayList<ArrayList<int[]>> resourceCoordinates;
    private final int[] kioskCoordinates;
    private static final int COORDINATE_DIMENSIONS = 2;

    private Map(char[][] grid, String[] legend, int[] kioskCoordinates) {
        this.grid = grid;
        this.legend = legend;
        this.mediaCoordinates = new ArrayList<>();
        this.resourceCoordinates = new ArrayList<>();
        this.kioskCoordinates = kioskCoordinates;
        checkMap();
    }

    public static class MapBuilder {
        private char[][]  grid;
        private String[] legend;
        private int[] kioskCoordinates;

        public MapBuilder() {}

        public MapBuilder grid(char[][] grid) throws Exception {
            if (grid == null) {
                throw new Exception("Grid should not be null.");
            }
            this.grid = grid;
            return this;
        }

        public static char[][] gridFromString(String mapData) throws Exception {
            if(mapData == null || mapData.isEmpty()) {
                throw new Exception("Map Data should not be null or empty.");
            }

            try (Scanner scanner = new Scanner(mapData)) {
                // Read dimensions
                String line = scanner.nextLine();
                String[] tokens = line.split(" ");
                int rows = Integer.parseInt(tokens[0]);
                int columns = Integer.parseInt(tokens[1]);

                // Create grid
                char[][] grid = new char[rows][columns];
                int row = 0;

                // Read grid data
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();

                    tokens = line.split(" ");
                    if (tokens.length != columns) {
                        throw new Exception("Row " + row + " has " + tokens.length + " columns, expected " + columns);
                    }

                    for (int col = 0; col < tokens.length; col++) {
                        // Take first character of each token
                        grid[row][col] = tokens[col].charAt(0);
                    }
                    row++;
                }
                if (row != rows) {
                    throw new Exception("Expected " + rows + " rows, but found " + row);
                }
                return grid;
            } catch (NumberFormatException e) {
                throw new Exception("Invalid dimensions format", e);
            } catch (Exception e) {
                throw new Exception("Error parsing map data: " + e.getMessage(), e);
            }
        }

        public MapBuilder legend(String[] legend) throws Exception {
            if(legend == null) {
                throw new Exception("Legend should not be null.");
            }
            this.legend = legend;
            return this;
        }

        public MapBuilder kioskCoordinates(int[] kioskCoordinates) throws Exception {
            if(kioskCoordinates == null) {
                throw new Exception("Kiosk coordinates should not be null.");
            }
            this.kioskCoordinates = kioskCoordinates;
            return this;
        }

        public Map build() {
            return new Map(grid, legend, kioskCoordinates);
        }
    }

    private void checkMap() {
        Preconditions.checkState(grid != null, "Grid should not be null.");
        Preconditions.checkState(legend != null, "Legend should not be null.");
        Preconditions.checkState(legend.length >= 1, "Legend should have at least one symbol.");
        Preconditions.checkState(kioskCoordinates != null, "Kiosk coordinates should not be null.");
        Preconditions.checkState(kioskCoordinates.length == 2, "Kiosk coordinates should have exactly 2 values.");
        Preconditions.checkState(mediaCoordinates != null, "Media coordinates list should not be null.");
        Preconditions.checkState(resourceCoordinates != null, "Resource coordinates list should not be null.");

        for (char[] chars : grid) {
            Preconditions.checkState(chars != null, "Grid row should not be null.");
        }

        for (String symbol : legend) {
            Preconditions.checkState(symbol != null, "Individual legend symbols should never be null.");
            Preconditions.checkState(symbol.contains(","), "Individual legend symbols should contain comma separated Symbol-Value pairs.");
        }

        for (int[] coordinate : mediaCoordinates) {
            Preconditions.checkState(coordinate != null, "Individual media coordinates should never be null.");
            Preconditions.checkState(coordinate.length == 2, "Each media coordinate should have exactly 2 values.");
        }

        for (ArrayList<int[]> resourceCoordList : resourceCoordinates) {
            Preconditions.checkState(resourceCoordList != null, "Resource coordinate list should not be null.");
            Preconditions.checkState(resourceCoordList.size() >= 1, "Resource coordinate list should have at least one coordinate.");

            for (int[] coordinate : resourceCoordList) {
                Preconditions.checkState(coordinate != null, "Individual resource coordinates should never be null.");
                Preconditions.checkState(coordinate.length == 2, "Each resource coordinate should have exactly 2 values.");
            }
        }
    }

    public boolean addMediaCoordinates(int[] coordinates) {
        checkMap();
        Preconditions.checkNotNull(coordinates, "Coordinates cannot be null");
        Preconditions.checkState(coordinates.length == 2, "Coordinates should have exactly 2 values (row, column)");

        int row = coordinates[0];
        int col = coordinates[1];

        // Validate coordinates are within grid bounds
        Preconditions.checkState(row >= 0 && row < grid.length, "Row coordinate out of bounds");
        Preconditions.checkState(col >= 0 && col < grid[0].length, "Column coordinate out of bounds");

        // Check that this coordinate doesn't already have a resource
        for (ArrayList<int[]> resourceCoordList : resourceCoordinates) {
            for (int[] resourceCoord : resourceCoordList) {
                if (resourceCoord[0] == row && resourceCoord[1] == col) {
                    return false; // Cannot add media where resource exists
                }
            }
        }

        boolean added = mediaCoordinates.add(coordinates);
        checkMap();
        return added;
    }

    public boolean addResourceCoordinates(ArrayList<int[]> coordinates) {
        checkMap();
        Preconditions.checkNotNull(coordinates, "Coordinates cannot be null");
        Preconditions.checkState(coordinates.size() >= 1, "Coordinates should have at least one coordinate");

        // Validate all coordinates first
        for (int[] coordinate : coordinates) {
            Preconditions.checkNotNull(coordinate, "Individual coordinate cannot be null");
            Preconditions.checkState(coordinate.length == 2, "Each coordinate should have exactly 2 values (row, column)");

            int row = coordinate[0];
            int col = coordinate[1];

            // Validate coordinates are within grid bounds
            Preconditions.checkState(row >= 0 && row < grid.length, "Row coordinate out of bounds");
            Preconditions.checkState(col >= 0 && col < grid[0].length, "Column coordinate out of bounds");
        }

        boolean added = resourceCoordinates.add(coordinates);
        checkMap();
        return added;
    }

    public ArrayList<int[]> findMedia(Media media) {
        checkMap();
        int[] targetCoordinates = media.getCoordinates();
        boolean mediaInMap = false;

        for (int[] coord : mediaCoordinates) {
            if (coord[0] == targetCoordinates[0] && coord[1] == targetCoordinates[1]) {
                mediaInMap = true;
                break;
            }
        }

        if (!mediaInMap) {
            return null; // Return null instead of printing
        }

        ArrayList<int[]> path = findPath(targetCoordinates, false);
        checkMap();
        return path;
    }

    public ArrayList<int[]> findResource(Resource resource) {
        checkMap();
        ArrayList<int[]> targetCoordinates = resource.getCoordinates();
        boolean resourceInMap = false;

        for (ArrayList<int[]> resourceCoordList : resourceCoordinates) {
            if (coordinateListsMatch(resourceCoordList, targetCoordinates)) {
                resourceInMap = true;
                break;
            }
        }

        if (!resourceInMap) {
            return null; // Return null instead of printing
        }

        ArrayList<int[]> path = findPath(targetCoordinates, true);
        checkMap();
        return path;
    }


    // Helper method to check if two coordinate lists match
    private boolean coordinateListsMatch(ArrayList<int[]> list1, ArrayList<int[]> list2) {
        boolean found = false;
        if (list1.size() != list2.size()) {
            return false;
        }

        for (int[] coord1 : list1) {
            for (int[] coord2 : list2) {
                if (coord1[0] == coord2[0] && coord1[1] == coord2[1]) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    // pathfinding method using DFS
    private ArrayList<int[]> findPath(Object targetCoordinates, boolean isResource) {
        int rows = grid.length;
        int columns = grid[0].length;
        boolean[][] visited = new boolean[rows][columns];
        boolean[][] pushed = new boolean[rows][columns];
        LinkedListStack<int[]> toVisit = new LinkedListStack<>();
        ArrayList<int[]> path = new ArrayList<>();
        int[] curr = kioskCoordinates.clone(); // Clone to avoid modifying original
        boolean stuck = false;
        boolean found = false;

        updatePushed(curr, pushed);
        updateVisited(curr, visited);
        path.add(curr.clone()); // Add starting position

        while(!found && !stuck) {
            if(isResource ? foundTarget(curr, (ArrayList<int[]>) targetCoordinates) :
                    foundTarget(curr, (int[]) targetCoordinates)) {
                found = true;
            } else {
                if(directionIsAvailable("UP", curr, visited, pushed, grid, targetCoordinates, isResource)) {
                    int[] next = new int[COORDINATE_DIMENSIONS];
                    next[0] = curr[0] - 1;
                    next[1] = curr[1];
                    toVisit.push(next);
                    updatePushed(next, pushed);
                }
                if(directionIsAvailable("DOWN", curr, visited, pushed, grid, targetCoordinates, isResource)) {
                    int[] next = new int[COORDINATE_DIMENSIONS];
                    next[0] = curr[0] + 1;
                    next[1] = curr[1];
                    toVisit.push(next);
                    updatePushed(next, pushed);
                }
                if(directionIsAvailable("LEFT", curr, visited, pushed, grid, targetCoordinates, isResource)) {
                    int[] next = new int[COORDINATE_DIMENSIONS];
                    next[0] = curr[0];
                    next[1] = curr[1] - 1;
                    toVisit.push(next);
                    updatePushed(next, pushed);
                }
                if(directionIsAvailable("RIGHT", curr, visited, pushed, grid, targetCoordinates, isResource)) {
                    int[] next = new int[COORDINATE_DIMENSIONS];
                    next[0] = curr[0];
                    next[1] = curr[1] + 1;
                    toVisit.push(next);
                    updatePushed(next, pushed);
                }
                if(!toVisit.isEmpty()) {
                    curr = toVisit.pop();
                    updateVisited(curr, visited);
                    path.add(curr.clone()); // Track the path
                } else {
                    stuck = true;
                    path = new ArrayList<>();
                }
            }
        }

        return path;
    }

    // Overloaded foundTarget for media (single coordinate)
    public boolean foundTarget(int[] curr, int[] targetCoordinates) {
        return curr[0] == targetCoordinates[0] && curr[1] == targetCoordinates[1];
    }

    // Overloaded foundTarget for resources (multiple coordinates)
    private boolean foundTarget(int[] curr, ArrayList<int[]> targetCoordinates) {
        boolean foundTarget = false;
        for (int[] coord : targetCoordinates) {
            if (curr[0] == coord[0] && curr[1] == coord[1]) {
                foundTarget = true;
            }
        }
        return foundTarget;
    }

    private static boolean directionIsAvailable(String direction, int[] curr, boolean[][] visited,
                                               boolean[][] pushed, char[][] grid, Object targetCoordinate,
                                               boolean isResource) {
        boolean available = false;
        int[] next = new int[COORDINATE_DIMENSIONS];
        switch (direction) {
            case "UP":
                next[0] = curr[0] - 1;
                next[1] = curr[1];
                break;
            case "DOWN":
                next[0] = curr[0] + 1;
                next[1] = curr[1];
                break;
            case "LEFT":
                next[0] = curr[0];
                next[1] = curr[1] - 1;
                break;
            case "RIGHT":
                next[0] = curr[0];
                next[1] = curr[1] + 1;
                break;
            default:
                System.out.println("Invalid input");
                break;
        }

        if (isResource) {
            ArrayList<int[]> resourceCoords = (ArrayList<int[]>) targetCoordinate;
            available = (grid[next[0]][next[1]] == '.' && !visited(next, visited) && !pushed(next, pushed)) ||
                    isAtResourceLocation(next, resourceCoords);
        } else {
            int[] mediaCoord = (int[]) targetCoordinate;
            available = (grid[next[0]][next[1]] == '.' && !visited(next, visited) && !pushed(next, pushed)) ||
                    (next[0] == mediaCoord[0] && next[1] == mediaCoord[1]);
        }

        return available;
    }

    // check if a position is at any resource location
    private static boolean isAtResourceLocation(int[] position, ArrayList<int[]> resourceCoords) {
        for (int[] coord : resourceCoords) {
            if (position[0] == coord[0] && position[1] == coord[1]) {
                return true;
            }
        }
        return false;
    }

    private static boolean visited(int[] toVisit, boolean[][] visited) {
        return visited[toVisit[0]][toVisit[1]];
    }

    private static boolean pushed(int[] toVisit, boolean[][] pushed) {
        return pushed[toVisit[0]][toVisit[1]];
    }

    private static void updatePushed(int[] curr, boolean[][] pushed) {
        pushed[curr[0]][curr[1]] = true;
    }

    private static void updateVisited(int[] curr, boolean[][] visited) {
        visited[curr[0]][curr[1]] = true;
    }

    private static void updateGrid(int[] curr, char[][] grid) {
        grid[curr[0]][curr[1]] = '*';
    }

    private char[][] deepCopy(char[][] original) {
        char[][] copy = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    private String gridPath(char[][] grid, int[] curr) {
        String mazePath = "";
        for(int row = 0; row < grid.length; row++) {
            for(int clmn = 0; clmn < grid[0].length; clmn++) {
                if(row == kioskCoordinates[0] && clmn == kioskCoordinates[1]) {
                    mazePath += "U ";
                }else if (row == curr[0] && clmn == curr[1]) {
                    mazePath += "X ";
                } else {
                    mazePath += grid[row][clmn] + " ";
                }
            }
            mazePath += "\n";
        }
        return mazePath;
    }

    public char[][] getGrid() {
        checkMap();
        return grid;
    }

    public String[] getLegend() {
        checkMap();
        return legend;
    }

    public int[] getKioskCoordinates() {
        return kioskCoordinates;
    }
}
