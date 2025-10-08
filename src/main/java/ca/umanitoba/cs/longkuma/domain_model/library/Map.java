package ca.umanitoba.cs.longkuma.domain_model.library;

import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class Map {
    final MapCell[][]  grid;
    final char[] legend = new char[]{
        'W',
        'E',
        'S',
        'A',
        'B',
        'F',
        'C',
        'H'
    };

    public Map() {
        grid = new MapCell[8][8];
    }

    private void checkMap() {
        Preconditions.checkState(grid != null, "Grid should not be null.");
        Preconditions.checkState(legend != null, "Legend should not be null.");
        Preconditions.checkState(legend.length >= 1, "Legend should have at least one symbol.");

        for (int i = 0; i < grid.length; i++) {
            Preconditions.checkState(grid[i] != null, "Grid row should not be null.");
        }

        for (Character symbol : legend) {
            Preconditions.checkState(symbol != null, "Individual legend symbols should never be null.");
        }
    }

    public MapCell[][] getGrid() {
        checkMap();
        return grid;
    }

    public char[] getLegend() {
        checkMap();
        return legend;
    }
}
