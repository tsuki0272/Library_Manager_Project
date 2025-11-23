package ca.umanitoba.cs.longkuma.logic.library;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Scanner;

public class Map {
    private final char[][]  grid;
    private final String[] legend;
    private ArrayList<int[]> mediaCoodinates;
    private ArrayList<ArrayList<int[]>> resourceCoodinates;
    private final int[] kioskCoordinates;

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

    public char[][] getGrid() {
        checkMap();
        return grid;
    }

    public String[] getLegend() {
        checkMap();
        return legend;
    }
}
