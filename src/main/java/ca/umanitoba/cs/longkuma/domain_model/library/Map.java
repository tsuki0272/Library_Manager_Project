package ca.umanitoba.cs.longkuma.domain_model.library;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Scanner;

public class Map {
    private final char[][]  grid;
    private String[] legend;

    private Map(char[][] grid, String[] legend) {
        this.grid = grid;
        this.legend = legend;
    }

    public static class MapBuilder {
        private char[][]  grid;
        private String[] legend;

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

        public Map build() {
            return new Map(grid, legend);
        }
    }

    private void checkMap() {
        Preconditions.checkState(grid != null, "Grid should not be null.");
        Preconditions.checkState(legend != null, "Legend should not be null.");
        Preconditions.checkState(legend.length >= 1, "Legend should have at least one symbol.");

        for (int i = 0; i < grid.length; i++) {
            Preconditions.checkState(grid[i] != null, "Grid row should not be null.");
        }

        for (String symbol : legend) {
            Preconditions.checkState(symbol != null, "Individual legend symbols should never be null.");
            Preconditions.checkState(symbol.contains(","), "Individual legend symbols should contain comma separated Symbol-Value pairs.");
        }
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
