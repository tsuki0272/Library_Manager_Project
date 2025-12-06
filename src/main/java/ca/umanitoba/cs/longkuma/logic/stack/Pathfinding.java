package ca.umanitoba.cs.longkuma.logic.stack;

import ca.umanitoba.cs.longkuma.domain.library.Map;
import ca.umanitoba.cs.longkuma.domain.media.Media;
import ca.umanitoba.cs.longkuma.domain.resource.Resource;
import ca.umanitoba.cs.longkuma.domain.stack.LinkedListStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Pathfinding {

    /*
     * Finds the path from the kiosk to a media item on the library map
     * Validates that the media coordinates exist in the map before pathfinding
     *
     * @param map The library map containing the grid and coordinates
     * @param media The media item to find a path to
     * @return A list of coordinate arrays representing the path, or null if media not found in map
     */
    public static List<int[]> findMediaPath(Map map, Media media) {
        int[] target = media.getCoordinates();
        if (!containsCoordinates(map.getMediaCoordinates(), target)) return null;
        return findPath(map, target, false);
    }

    /*
     * Finds the path from the kiosk to a resource on the library map
     * Validates that the resource coordinates exist in the map before pathfinding
     *
     * @param map The library map containing the grid and coordinates
     * @param resource The resource to find a path to
     * @return A list of coordinate arrays representing the path, or null if resource not found in map
     */
    public static List<int[]> findResourcePath(Map map, Resource resource) {
        List<int[]> target = resource.getCoordinates();
        if (!containsResource(map.getResourceCoordinates(), target)) return null;
        return findPath(map, target, true);
    }

    /*
     * Performs depth-first search to find a path from kiosk to target coordinates
     * Uses a stack-based iterative approach with parent tracking for path reconstruction
     * Handles both single-point targets (media) and multi-point targets (resources)
     *
     * @param map The library map containing the grid and kiosk coordinates
     * @param targetCoordinates The target coordinates (int[] for media, ArrayList<int[]> for resources)
     * @param isResource True if target is a resource with multiple coordinates, false for media
     * @return A list of coordinate arrays representing the path from kiosk to target, or empty list if no path exists
     */
    private static ArrayList<int[]> findPath(Map map, Object targetCoordinates, boolean isResource) {
        char[][] grid = map.getGrid();
        int[] start = map.getKioskCoordinates();
        int rows = grid.length;
        int cols = grid[0].length;

        boolean[][] visited = new boolean[rows][cols];
        LinkedListStack<int[]> stack = new LinkedListStack<>();
        HashMap<String, int[]> parent = new HashMap<>();

        stack.push(start.clone());
        visited[start[0]][start[1]] = true;

        int[] targetSingle = isResource ? null : (int[]) targetCoordinates;
        ArrayList<int[]> targetMulti = isResource ? (ArrayList<int[]>) targetCoordinates : null;

        int[] foundTarget = null;

        while (!stack.isEmpty()) {
            int[] curr = stack.pop();

            if (isResource) {
                if (atResource(curr, targetMulti)) {
                    foundTarget = curr;
                    break;
                }
            } else {
                if (atTarget(curr, targetSingle)) {
                    foundTarget = curr;
                    break;
                }
            }

            for (int[] dir : new int[][]{{-1,0},{1,0},{0,-1},{0,1}}) {
                int nr = curr[0] + dir[0];
                int nc = curr[1] + dir[1];
                if (nr >= 0 && nc >= 0 && nr < rows && nc < cols && !visited[nr][nc]) {
                    char cell = grid[nr][nc];
                    if (cell == '.' || (isResource && atResource(new int[]{nr,nc}, targetMulti)) || (!isResource && atTarget(new int[]{nr,nc}, targetSingle))) {
                        int[] next = new int[]{nr,nc};
                        stack.push(next);
                        visited[nr][nc] = true;
                        parent.put(nr + "," + nc, curr);
                    }
                }
            }
        }

        if (foundTarget == null) return new ArrayList<>(); // no path found

        // Reconstruct path
        ArrayList<int[]> path = new ArrayList<>();
        int[] step = foundTarget;
        while (step != null) {
            path.add(0, step);
            step = parent.get(step[0] + "," + step[1]);
        }

        return path;
    }

    /*
     * Checks if current coordinates match the target coordinates
     *
     * @param curr The current coordinate array
     * @param target The target coordinate array
     * @return true if coordinates match, false otherwise
     */
    private static boolean atTarget(int[] curr, int[] target) {
        return curr[0] == target[0] && curr[1] == target[1];
    }

    /*
     * Checks if current coordinates match any of the resource's target coordinates
     *
     * @param curr The current coordinate array
     * @param targets The list of target coordinate arrays for the resource
     * @return true if current coordinates match any target, false otherwise
     */
    private static boolean atResource(int[] curr, ArrayList<int[]> targets) {
        for (int[] t : targets) {
            if (curr[0] == t[0] && curr[1] == t[1]) return true;
        }
        return false;
    }

    /*
     * Checks if a coordinate exists in a list of coordinates
     *
     * @param list The list of coordinate arrays to search
     * @param coord The coordinate array to find
     * @return true if the coordinate exists in the list, false otherwise
     */
    private static boolean containsCoordinates(List<int[]> list, int[] coord) {
        for (int[] c : list) if (c[0] == coord[0] && c[1] == coord[1]) return true;
        return false;
    }

    /*
     * Checks if a resource's coordinates match any resource in the map's resource list
     * Compares all coordinate pairs to ensure complete match
     *
     * @param list The list of resource coordinate arrays from the map
     * @param target The target resource's coordinate list to find
     * @return true if the resource exists in the map with matching coordinates, false otherwise
     */
    private static boolean containsResource(List<ArrayList<int[]>> list, List<int[]> target) {
        // Check if any resource in the map matches all coordinates in target
        for (ArrayList<int[]> resCoords : list) {
            if (resCoords.size() != target.size()) continue;
            boolean match = true;
            for (int i = 0; i < resCoords.size(); i++) {
                int[] a = resCoords.get(i);
                int[] b = target.get(i);
                if (a[0] != b[0] || a[1] != b[1]) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }
        return false;
    }
}