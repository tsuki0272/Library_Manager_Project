package ca.umanitoba.cs.longkuma.domain_model.member;

import ca.umanitoba.cs.longkuma.domain_model.media.MediaCopy;
import ca.umanitoba.cs.longkuma.domain_model.resource.Resource;
import com.google.common.base.Preconditions;

import java.util.ArrayList;

public class Member {
    private ArrayList<MediaCopy> borrowedMedia;
    private ArrayList<Resource> resources;
    private ArrayList<Constraint> constraints;
    private String name;

    public Member(String name) {
        this.name = name;
        this.borrowedMedia = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.constraints = new ArrayList<>();
        checkMember();
    }

    private void checkMember() {
        Preconditions.checkState(name != null, "Member name should not be null.");
        Preconditions.checkState(name.length() >= 1, "Member name should have at least one symbol.");
        Preconditions.checkState(borrowedMedia != null, "Borrowed media list should not be null.");
        Preconditions.checkState(resources != null, "Resources list should not be null.");
        Preconditions.checkState(constraints != null, "Constraints list should not be null.");

        for (MediaCopy copy : borrowedMedia) {
            Preconditions.checkState(copy != null, "Individual media copies should never be null.");
        }

        for (Resource resource : resources) {
            Preconditions.checkState(resource != null, "Individual resources should never be null.");
        }

        for (Constraint constraint : constraints) {
            Preconditions.checkState(constraint != null, "Individual constraints should never be null.");
        }
    }

    public String getName() {
        return name;
    }

    public boolean addMediaCopy(MediaCopy copy) {
        checkMember();
        Preconditions.checkNotNull(copy, "Media copy cannot be null");
        boolean added = borrowedMedia.add(copy);
        checkMember();
        return added;
    }

    public boolean addResource(Resource resource) {
        checkMember();
        Preconditions.checkNotNull(resource, "Resource cannot be null");
        boolean added = resources.add(resource);
        checkMember();
        return added;
    }

    public boolean addConstraint(Constraint constraint) {
        checkMember();
        Preconditions.checkNotNull(constraint, "Constraint cannot be null");
        boolean added = constraints.add(constraint);
        checkMember();
        return added;
    }
}
