package ca.umanitoba.cs.longkuma.domain.library;

import ca.umanitoba.cs.longkuma.domain.media.Media;
import ca.umanitoba.cs.longkuma.domain.exceptions.InvalidMapException;
import ca.umanitoba.cs.longkuma.domain.exceptions.InvalidNameException;
import ca.umanitoba.cs.longkuma.domain.resource.Resource;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class Library {
    final private String name;
    final private List<Media> media;
    final private ArrayList<Resource> resources;
    final private Map map;

    /*
     * Private constructor for Library
     * Initializes library with name and map, creates empty media and resource lists
     *
     * @param name The name of the library
     * @param map The map layout of the library
     */
    private Library(String name, Map map) {
        this.name = name;
        this.media = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.map = map;
    }

    public static class LibraryBuilder {
        private String name;
        private Map map;

        public LibraryBuilder() {
        }

        /*
         * Sets the name for the library being built
         *
         * @param name The name of the library
         * @return LibraryBuilder instance for method chaining
         * @throws Exception if name is null or empty
         */
        public LibraryBuilder name(String name) throws InvalidNameException {
            if(name == null || name.isEmpty()) {
                throw new InvalidNameException("Name should not be null or empty.");
            }
            this.name = name;
            return this;
        }

        /*
         * Sets the map for the library being built
         *
         * @param map The map layout of the library
         * @return LibraryBuilder instance for method chaining
         * @throws Exception if map is null
         */
        public LibraryBuilder map(Map map) throws InvalidMapException {
            if(map == null) {
                throw new InvalidMapException("Map should not be null.");
            }
            this.map = map;
            return this;
        }

        /*
         * Builds and returns a new Library instance with configured parameters
         *
         * @return A new Library object
         */
        public Library build() {
            return new Library(name, map);
        }
    }

    /*
     * Validates the internal state of the Library object
     * Ensures all required fields are non-null and meet minimum requirements
     * Checks that all media and resources in collections are non-null
     */
    private void checkLibrary() {
        Preconditions.checkState(name != null, "Library name should not be null.");
        Preconditions.checkState(!name.isEmpty(), "Library name should have at least one symbol.");
        Preconditions.checkState(true, "Media list should not be null.");
        Preconditions.checkState(true, "Resources list should not be null.");
        Preconditions.checkState(map != null, "Map should not be null.");

        for (Media m : media) {
            Preconditions.checkState(m != null, "Individual media should never be null.");
        }

        for (Resource r : resources) {
            Preconditions.checkState(r != null, "Individual resources should never be null.");
        }
    }

    // Getters:
    public String getName() {
        return name;
    }

    public List<Media> getMedia() {
        return media;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public Map getMap() {
        return map;
    }

    /*
     * Adds a new media item to the library's collection
     * Validates library state before and after adding
     *
     * @param media The media item to add
     * @return true if media was successfully added, false otherwise
     */
    public void addMedia(Media media) {
        checkLibrary();
        Preconditions.checkNotNull(media, "Media cannot be null");

        this.media.add(media);

        checkLibrary();
    }

    /*
     * Adds a new resource to the library's collection
     * Validates library state before and after adding
     *
     * @param resource The resource to add
     * @return true if resource was successfully added, false otherwise
     */
    public void addResource(Resource resource) {
        checkLibrary();
        Preconditions.checkNotNull(resource, "Resource cannot be null");

        resources.add(resource);

        checkLibrary();
    }
}