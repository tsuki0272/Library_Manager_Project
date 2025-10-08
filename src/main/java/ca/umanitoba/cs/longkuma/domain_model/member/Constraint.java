package ca.umanitoba.cs.longkuma.domain_model.member;

import com.google.common.base.Preconditions;

public class Constraint {
    private String constraint;

    public Constraint(String constraint) {
        this.constraint = constraint;
    }

    private void checkConstraint() {
        Preconditions.checkState(constraint != null, "Constraint should not be null.");
        Preconditions.checkState(constraint.length() >= 1, "Constraint should have at least one symbol.");
    }


    public String getConstraint() {
        checkConstraint();
        return constraint;
    }
}
