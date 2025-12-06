package ca.umanitoba.cs.longkuma.domain.library;

import ca.umanitoba.cs.longkuma.domain.exceptions.InvalidMapException;
import ca.umanitoba.cs.longkuma.domain.exceptions.MapDimensionMismatchException;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Map {

    private final char[][] grid;
    private final String[] legend;
    private final int[] kioskCoordinates;
    private final List<int[]> mediaCoordinates;
    private final List<ArrayList<int[]>> resourceCoordinates;
    private static final int COORDINATE_DIMENSIONS = 2;

    private Map(char[][] grid, String[] legend, int[] kioskCoordinates) {
        this.grid = grid;
        this.legend = legend;
        this.kioskCoordinates = kioskCoordinates;
        this.mediaCoordinates = new ArrayList<>();
        this.resourceCoordinates = new ArrayList<>();
        checkMap();
    }

    /** ------------------ Builder ------------------ **/
    public static class MapBuilder {
        private char[][] grid;
        private String[] legend;
        private int[] kioskCoordinates;

        public MapBuilder grid(char[][] grid) {
            Preconditions.checkNotNull(grid, "Grid cannot be null");
            this.grid = grid;
            return this;
        }

        public MapBuilder legend(String[] legend) {
            Preconditions.checkNotNull(legend, "Legend cannot be null");
            this.legend = legend;
            return this;
        }

        public MapBuilder kioskCoordinates(int[] kioskCoordinates) {
            Preconditions.checkNotNull(kioskCoordinates, "Kiosk cannot be null");
            this.kioskCoordinates = kioskCoordinates;
            return this;
        }

        public Map build() {
            return new Map(grid, legend, kioskCoordinates);
        }
    }

    /** ------------------ Domain Utilities ------------------ **/
    public static char[][] gridFromString(String mapData) throws MapDimensionMismatchException, InvalidMapException {
        if (mapData == null || mapData.isEmpty()) {
            throw new InvalidMapException("Map Data cannot be null or empty");
        }

        try (Scanner scanner = new Scanner(mapData)) {
            String[] dims = scanner.nextLine().split(" ");
            int rows = Integer.parseInt(dims[0]);
            int cols = Integer.parseInt(dims[1]);

            char[][] grid = new char[rows][cols];
            int row = 0;

            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(" ");
                if (tokens.length != cols) throw new MapDimensionMismatchException("Row " + row + " has wrong number of columns");
                for (int col = 0; col < cols; col++) {
                    grid[row][col] = tokens[col].charAt(0);
                }
                row++;
            }

            if (row != rows) throw new MapDimensionMismatchException("Expected " + rows + " rows, found " + row);
            return grid;
        }
    }

    /** ------------------ Getters and Setters ------------------ **/
    public char[][] getGrid() { checkMap(); return grid; }
    public String[] getLegend() { checkMap(); return legend; }
    public int[] getKioskCoordinates() { return kioskCoordinates; }

    public List<int[]> getMediaCoordinates() { return mediaCoordinates; }
    public List<ArrayList<int[]>> getResourceCoordinates() { return resourceCoordinates; }

    public boolean addMediaCoordinates(int[] coordinates) {
        Preconditions.checkNotNull(coordinates, "Coordinates cannot be null");
        Preconditions.checkState(coordinates.length == COORDINATE_DIMENSIONS, "Coordinates must have length 2");
        mediaCoordinates.add(coordinates);
        return true;
    }

    public boolean addResourceCoordinates(ArrayList<int[]> coordinates) {
        Preconditions.checkNotNull(coordinates, "Coordinates cannot be null");
        Preconditions.checkState(coordinates.size() >= 1, "Resource must have at least one coordinate");
        resourceCoordinates.add(coordinates);
        return true;
    }

    private void checkMap() {
        Preconditions.checkState(grid != null, "Grid cannot be null");
        Preconditions.checkState(legend != null && legend.length >= 1, "Legend invalid");
        Preconditions.checkState(kioskCoordinates != null && kioskCoordinates.length == 2, "Kiosk invalid");
        Preconditions.checkState(mediaCoordinates != null, "Media list cannot be null");
        Preconditions.checkState(resourceCoordinates != null, "Resource list cannot be null");
    }
}
