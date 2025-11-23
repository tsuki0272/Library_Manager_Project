package ca.umanitoba.cs.longkuma.logic.member;

import com.google.common.base.Preconditions;

public class Constraint {
    final private String constraint;

    private Constraint(String constraint) {
        this.constraint = constraint;
    }

    public static class ConstraintBuilder {
        private String constraint;

        public ConstraintBuilder() {}

        public ConstraintBuilder constraint(String constraint) throws Exception {
            if (constraint == null || constraint.isEmpty()) {
                throw new Exception("Constraint should not be null or empty.");
            }
            this.constraint = constraint;
            return this;
        }

        public Constraint build() {
            return new Constraint(constraint);
        }
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
