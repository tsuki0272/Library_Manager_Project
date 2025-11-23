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
    private ArrayList<int[]> mediaCoodinates;
    private ArrayList<ArrayList<int[]>> resourceCoodinates;
    private final int[] kioskCoordinates;
    private static final int COORDINATE_DIMENSIONS = 2;

    private Map(char[][] grid, String[] legend, int[] kioskCoordinates) {
        this.grid = grid;
        this.legend = legend;
        this.mediaCoodinates = new ArrayList<>();
        this.resourceCoodinates = new ArrayList<>();
        this.kioskCoordinates = kioskCoordinates;
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
        Preconditions.checkState(mediaCoodinates != null, "Media coordinates list should not be null.");
        Preconditions.checkState(resourceCoodinates != null, "Resource coordinates list should not be null.");

        for (char[] chars : grid) {
            Preconditions.checkState(chars != null, "Grid row should not be null.");
        }

        for (String symbol : legend) {
            Preconditions.checkState(symbol != null, "Individual legend symbols should never be null.");
            Preconditions.checkState(symbol.contains(","), "Individual legend symbols should contain comma separated Symbol-Value pairs.");
        }

        for (int[] coordinate : mediaCoodinates) {
            Preconditions.checkState(coordinate != null, "Individual media coordinates should never be null.");
            Preconditions.checkState(coordinate.length == 2, "Each media coordinate should have exactly 2 values.");
        }

        for (ArrayList<int[]> resourceCoordList : resourceCoodinates) {
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
        for (ArrayList<int[]> resourceCoordList : resourceCoodinates) {
            for (int[] resourceCoord : resourceCoordList) {
                if (resourceCoord[0] == row && resourceCoord[1] == col) {
                    return false; // Cannot add media where resource exists
                }
            }
        }

        boolean added = mediaCoodinates.add(coordinates);
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

        boolean added = resourceCoodinates.add(coordinates);
        checkMap();
        return added;
    }

    public void findMedia(Media media) {
        int rows = grid.length;
        int columns = grid[0].length;
        boolean[][] visited = new boolean[rows][columns];
        boolean[][] pushed = new boolean[rows][columns];
        LinkedListStack<int[]> toVisit = new LinkedListStack<>();
        int[] curr = kioskCoordinates;
        boolean stuck = false;
        boolean found = false;
        char[][] gridCopy = deepCopy(grid);
        int[] targetCoordinates = media.getCoordinates();

        updatePushed(curr, pushed);
        updateVisited(curr, visited);

        while(!found && !stuck) {
            if(foundExit(curr, targetCoordinates)) {
                found = true;
                System.out.println(gridPath(gridCopy));
            } else {
                updateGrid(curr, gridCopy);
                if(directionIsAvailable("UP", curr, visited, pushed, gridCopy, targetCoordinates)) {
                    int[] next = new int[COORDINATE_DIMENSIONS];
                    next[0] = curr[0] - 1;
                    next[1] = curr[1];
                    toVisit.push(next);
                    updatePushed(next, pushed);
                }
                if(directionIsAvailable("DOWN", curr, visited, pushed, gridCopy, targetCoordinates)) {
                    int[] next = new int[COORDINATE_DIMENSIONS];
                    next[0] = curr[0] + 1;
                    next[1] = curr[1];
                    toVisit.push(next);
                    updatePushed(next, pushed);
                }
                if(directionIsAvailable("LEFT", curr, visited, pushed, gridCopy, targetCoordinates)) {
                    int[] next = new int[COORDINATE_DIMENSIONS];
                    next[0] = curr[0];
                    next[1] = curr[1] - 1;
                    toVisit.push(next);
                    updatePushed(next, pushed);
                }
                if(directionIsAvailable("RIGHT", curr, visited, pushed, gridCopy, targetCoordinates)) {
                    int[] next = new int[COORDINATE_DIMENSIONS];
                    next[0] = curr[0];
                    next[1] = curr[1] + 1;
                    toVisit.push(next);
                    updatePushed(next, pushed);
                }
                if(!toVisit.isEmpty()) {
                    curr = toVisit.pop();
                    updateVisited(curr, visited);
                } else {
                    stuck = true;
                    System.out.println("There's no way to get to this media!");
                }
            }
        }
    }

    public void findResource(Resource resource) {

    }

    public static boolean directionIsAvailable(String direction, int[] curr, boolean[][] visited, boolean[][] pushed, char[][] grid, int[] targetCoordinate) {
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

        available = (grid[next[0]][next[1]] == '.' && !visited(next, visited) && !pushed(next, pushed)) ||
                (next[0] == targetCoordinate[0] && next[1] == targetCoordinate[1]);

        return available;
    }

    public static boolean visited(int[] toVisit, boolean[][] visited) {
        return visited[toVisit[0]][toVisit[1]];
    }

    public static boolean pushed(int[] toVisit, boolean[][] pushed) {
        return pushed[toVisit[0]][toVisit[1]];
    }

    private static void updatePushed(int[] curr, boolean[][] pushed) {
        pushed[curr[0]][curr[1]] = true;
    }

    private static void updateVisited(int[] curr, boolean[][] visited) {
        visited[curr[0]][curr[1]] = true;
    }

    public boolean foundExit(int[] curr, int[] targetCoordinates) {
        return curr[0] == targetCoordinates[0] && curr[1] == targetCoordinates[1];
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

    private static String gridPath(char[][] grid) {
        String mazePath = "";
        for(int row = 0; row < grid.length; row++) {
            for(int clmn = 0; clmn < grid[0].length; clmn++) {
                mazePath += grid[row][clmn] + " ";
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

    public static void main(String[] args) {
        try {
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
            char[][] mapGrid1 = null;
            mapGrid1 = MapBuilder.gridFromString(mapString1);
            Map map1 = new MapBuilder().grid(mapGrid1).legend(legend1).kioskCoordinates(kiosk1).build();

            Media media1 = new Media.MediaBuilder().title("The Hobbit").author("J.R.R. Tolkien")
                    .type("Book").coordinates(new int[]{2,3}).build();
            MediaCopy copy1 = new MediaCopy.MediaCopyBuilder().copyNumber(1).media(media1).build();
            media1.addCopy(copy1);

            Media media2 = new Media.MediaBuilder().title("Thriller").author("Michael Jackson")
                    .type("CD").coordinates(new int[]{2,2}).build();
            MediaCopy copy2 = new MediaCopy.MediaCopyBuilder().copyNumber(1).media(media2).build();
            media2.addCopy(copy2);

            map1.addMediaCoordinates(new int[]{2,2});
            map1.addMediaCoordinates(new int[]{2,3});

//            for(int i = 0; i < map1.grid.length; i++) {
//                for(int j = 0; j < map1.grid[0].length; j++) {
//                    System.out.print(map1.grid[i][j] + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();

            map1.findMedia(media1);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
