package ca.umanitoba.cs.longkuma.logic.library;

import ca.umanitoba.cs.longkuma.logic.media.Media;
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

    /*
     * Private constructor for Map
     * Initializes map with grid, legend, and kiosk location
     * Creates empty lists for media and resource coordinates
     *
     * @param grid 2D character array representing the library layout
     * @param legend Array of strings describing grid symbols
     * @param kioskCoordinates Coordinates of the starting point/kiosk [row, column]
     */
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

        /*
         * Sets the grid for the map being built
         *
         * @param grid 2D character array representing the library layout
         * @return MapBuilder instance for method chaining
         * @throws Exception if grid is null
         */
        public MapBuilder grid(char[][] grid) throws Exception {
            if (grid == null) {
                throw new Exception("Grid should not be null.");
            }
            this.grid = grid;
            return this;
        }

        /*
         * Parses a string representation of map data into a 2D grid
         * Expected format: First line contains "rows columns", followed by grid data
         * Grid cells are space-separated characters
         *
         * @param mapData String containing map dimensions and grid data
         * @return 2D character array representing the parsed grid
         * @throws Exception if mapData is null, empty, or has invalid format
         */
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

        /*
         * Sets the legend for the map being built
         * Legend entries should be in "Symbol,Description" format
         *
         * @param legend Array of strings describing grid symbols
         * @return MapBuilder instance for method chaining
         * @throws Exception if legend is null
         */
        public MapBuilder legend(String[] legend) throws Exception {
            if(legend == null) {
                throw new Exception("Legend should not be null.");
            }
            this.legend = legend;
            return this;
        }

        /*
         * Sets the kiosk coordinates for the map being built
         * Kiosk represents the starting point for pathfinding
         *
         * @param kioskCoordinates Coordinates [row, column] of the kiosk location
         * @return MapBuilder instance for method chaining
         * @throws Exception if kioskCoordinates is null
         */
        public MapBuilder kioskCoordinates(int[] kioskCoordinates) throws Exception {
            if(kioskCoordinates == null) {
                throw new Exception("Kiosk coordinates should not be null.");
            }
            this.kioskCoordinates = kioskCoordinates;
            return this;
        }

        /*
         * Builds and returns a new Map instance with configured parameters
         *
         * @return A new Map object
         */
        public Map build() {
            return new Map(grid, legend, kioskCoordinates);
        }
    }

    /*
     * Validates the internal state of the Map object
     * Ensures all required fields are non-null and meet requirements
     * Validates grid structure, legend format, and coordinate dimensions
     * Checks that all coordinates in collections are valid
     */
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

    // Getters:
    public char[][] getGrid() {
        checkMap();
        return grid;
    }

    public String[] getLegend() {
        checkMap();
        return legend;
    }

    /*
     * Adds media coordinates to the map
     * Validates that coordinates are within grid bounds and not occupied by resources
     * Multiple media can share the same coordinates
     *
     * @param coordinates Array of 2 integers [row, column] representing media location
     * @return true if coordinates were added successfully, false if location already has a resource
     */
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

    /*
     * Adds resource coordinates to the map
     * Resources can span multiple grid cells
     * Validates that all coordinates are within grid bounds
     *
     * @param coordinates ArrayList of coordinate arrays, each [row, column]
     * @return true if coordinates were added successfully
     */
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

    /*
     * Finds a path from the kiosk to the specified media location
     * Uses depth-first search algorithm to find path through walkable spaces
     *
     * @param media The media object to find path to
     * @return ArrayList of coordinates representing the path, null if media not in map, empty list if no path exists
     */
    public ArrayList<int[]> findMedia(Media media) {
        checkMap();
        int[] targetCoordinates = media.getCoordinates();
        boolean mediaInMap = false;
        ArrayList<int[]> path = null;

        for (int[] coord : mediaCoordinates) {
            if (coord[0] == targetCoordinates[0] && coord[1] == targetCoordinates[1]) {
                mediaInMap = true;
                break;
            }
        }

        if (!mediaInMap) {
            return path;
        }

        path = findPath(targetCoordinates, false);
        checkMap();
        return path;
    }

    /*
     * Finds a path from the kiosk to the specified resource location
     * Uses depth-first search algorithm to find path through walkable spaces
     * Resource can occupy multiple cells; path leads to any cell of the resource
     *
     * @param resource The resource object to find path to
     * @return ArrayList of coordinates representing the path, null if resource not in map, empty list if no path exists
     */
    public ArrayList<int[]> findResource(Resource resource) {
        checkMap();
        ArrayList<int[]> targetCoordinates = resource.getCoordinates();
        boolean resourceInMap = false;
        ArrayList<int[]> path = null;

        for (ArrayList<int[]> resourceCoordList : resourceCoordinates) {
            if (coordinateListsMatch(resourceCoordList, targetCoordinates)) {
                resourceInMap = true;
                break;
            }
        }

        if (!resourceInMap) {
            return path;
        }

        path = findPath(targetCoordinates, true);
        checkMap();
        return path;
    }

    /*
     * Checks if two coordinate lists match
     * Lists match if they contain the same coordinates (order-independent)
     *
     * @param list1 First list of coordinates
     * @param list2 Second list of coordinates
     * @return true if all coordinates in list1 exist in list2 and vice versa, false otherwise
     */
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

    /*
     * Performs depth-first search pathfinding from kiosk to target location
     * Only traverses walkable spaces (marked as '.' in grid)
     * Tracks visited nodes to avoid cycles
     *
     * @param targetCoordinates Either int[] for media or ArrayList<int[]> for resources
     * @param isResource true if finding path to resource, false if finding path to media
     * @return ArrayList of coordinates representing the path from kiosk to target, empty list if no path exists
     */
    private ArrayList<int[]> findPath(Object targetCoordinates, boolean isResource) {
        int rows = grid.length;
        int columns = grid[0].length;
        boolean[][] visited = new boolean[rows][columns];
        boolean[][] pushed = new boolean[rows][columns];
        LinkedListStack<int[]> toVisit = new LinkedListStack<>();
        ArrayList<int[]> path = new ArrayList<>();
        int[] curr = kioskCoordinates.clone();
        boolean stuck = false;
        boolean found = false;

        updatePushed(curr, pushed);
        updateVisited(curr, visited);
        path.add(curr.clone());

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
                    path.add(curr.clone());
                } else {
                    stuck = true;
                    path = new ArrayList<>();
                }
            }
        }

        return path;
    }

    /*
     * Checks if current position matches target coordinates for media
     *
     * @param curr Current position [row, column]
     * @param targetCoordinates Target position [row, column]
     * @return true if current position matches target, false otherwise
     */
    public boolean foundTarget(int[] curr, int[] targetCoordinates) {
        return curr[0] == targetCoordinates[0] && curr[1] == targetCoordinates[1];
    }

    /*
     * Checks if current position matches any of the target coordinates for resources
     * Resources can span multiple cells
     *
     * @param curr Current position [row, column]
     * @param targetCoordinates List of resource positions
     * @return true if current position matches any resource coordinate, false otherwise
     */
    private boolean foundTarget(int[] curr, ArrayList<int[]> targetCoordinates) {
        boolean foundTarget = false;
        for (int[] coord : targetCoordinates) {
            if (curr[0] == coord[0] && curr[1] == coord[1]) {
                foundTarget = true;
            }
        }
        return foundTarget;
    }

    /*
     * Checks if movement in specified direction is available
     * Movement is available if target cell is walkable, not visited, not already pushed to stack
     * or if target cell is the destination
     *
     * @param direction Direction to check ("UP", "DOWN", "LEFT", "RIGHT")
     * @param curr Current position [row, column]
     * @param visited 2D boolean array tracking visited cells
     * @param pushed 2D boolean array tracking cells already added to stack
     * @param grid 2D character array representing the map
     * @param targetCoordinate Target location (int[] for media, ArrayList<int[]> for resources)
     * @param isResource true if pathfinding to resource, false if pathfinding to media
     * @return true if movement in specified direction is available, false otherwise
     */
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

    /*
     * Checks if a position is at any of the resource's coordinates
     * Used to allow pathfinding to reach resource locations
     *
     * @param position Position to check [row, column]
     * @param resourceCoords List of resource coordinates
     * @return true if position matches any resource coordinate, false otherwise
     */
    private static boolean isAtResourceLocation(int[] position, ArrayList<int[]> resourceCoords) {
        boolean isAtResourceLocation = false;
        for (int[] coord : resourceCoords) {
            if (position[0] == coord[0] && position[1] == coord[1]) {
                isAtResourceLocation = true;
                break;
            }
        }
        return isAtResourceLocation;
    }

    /*
     * Checks if a coordinate has been visited during pathfinding
     *
     * @param toVisit Coordinate to check [row, column]
     * @param visited 2D boolean array tracking visited cells
     * @return true if coordinate has been visited, false otherwise
     */
    private static boolean visited(int[] toVisit, boolean[][] visited) {
        return visited[toVisit[0]][toVisit[1]];
    }

    /*
     * Checks if a coordinate has been pushed to the stack during pathfinding
     *
     * @param toVisit Coordinate to check [row, column]
     * @param pushed 2D boolean array tracking pushed cells
     * @return true if coordinate has been pushed, false otherwise
     */
    private static boolean pushed(int[] toVisit, boolean[][] pushed) {
        return pushed[toVisit[0]][toVisit[1]];
    }

    /*
     * Marks a coordinate as pushed to the stack during pathfinding
     *
     * @param curr Coordinate to mark [row, column]
     * @param pushed 2D boolean array tracking pushed cells
     */
    private static void updatePushed(int[] curr, boolean[][] pushed) {
        pushed[curr[0]][curr[1]] = true;
    }

    /*
     * Marks a coordinate as visited during pathfinding
     *
     * @param curr Coordinate to mark [row, column]
     * @param visited 2D boolean array tracking visited cells
     */
    private static void updateVisited(int[] curr, boolean[][] visited) {
        visited[curr[0]][curr[1]] = true;
    }

    /*
     * Gets the coordinates of the kiosk (starting point)
     *
     * @return Array of 2 integers [row, column] representing kiosk location
     */
    public int[] getKioskCoordinates() {
        return kioskCoordinates;
    }
}