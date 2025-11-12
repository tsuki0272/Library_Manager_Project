package ca.umanitoba.cs.longkuma.logic.library;

import ca.umanitoba.cs.longkuma.logic.media.Media;
import ca.umanitoba.cs.longkuma.logic.resource.Resource;
import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class Library {
    final private String name;
    final private ArrayList<Media> media;
    final private ArrayList<Resource> resources;
    final private Map map;

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

        public LibraryBuilder name(String name) throws Exception {
            if(name == null || name.isEmpty()) {
                throw new Exception("Name should not be null or empty.");
            }
            this.name = name;
            return this;
        }

        public LibraryBuilder map(Map map) throws Exception {
            if(map == null) {
                throw new Exception("Map should not be null.");
            }
            this.map = map;
            return this;
        }

        public Library build() {
            return new Library(name, map);
        }
    }

    private void checkLibrary() {
        Preconditions.checkState(name != null, "Library name should not be null.");
        Preconditions.checkState(name.length() >= 1, "Library name should have at least one symbol.");
        Preconditions.checkState(media != null, "Media list should not be null.");
        Preconditions.checkState(resources != null, "Resources list should not be null.");
        Preconditions.checkState(map != null, "Map should not be null.");

        for (Media m : media) {
            Preconditions.checkState(m != null, "Individual media should never be null.");
        }

        for (Resource r : resources) {
            Preconditions.checkState(r != null, "Individual resources should never be null.");
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<Media> getMedia() {
        return media;
    }

    public boolean addMedia(Media media) {
        checkLibrary();
        Preconditions.checkNotNull(media, "Media cannot be null");

        boolean added = this.media.add(media);

        checkLibrary();
        return added;
    }

    public Media showMedia(Media media) {
        checkLibrary();
        Preconditions.checkNotNull(media, "Media cannot be null");

        Media foundMedia = null;
        for (Media m : this.media) {
            if (m.equals(media)) {
                foundMedia = m;
                break;
            }
        }

        checkLibrary();
        return foundMedia;
    }

    public boolean removeMedia(Media media) {
        checkLibrary();
        Preconditions.checkNotNull(media, "Media cannot be null");

        boolean removed = this.media.remove(media);

        checkLibrary();
        return removed;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public boolean addResource(Resource resource) {
        checkLibrary();
        Preconditions.checkNotNull(resource, "Resource cannot be null");

        boolean added = resources.add(resource);

        checkLibrary();
        return added;
    }

    public Resource showResource(Resource resource) {
        checkLibrary();
        Preconditions.checkNotNull(resource, "Resource cannot be null");

        Resource foundResource = null;
        for (Resource r : this.resources) {
            if (r.equals(resource)) {
                foundResource = r;
                break;
            }
        }
        checkLibrary();
        return foundResource;
    }
}
