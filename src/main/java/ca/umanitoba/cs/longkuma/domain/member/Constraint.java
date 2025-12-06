package ca.umanitoba.cs.longkuma.domain.member;

import ca.umanitoba.cs.longkuma.domain.exceptions.InvalidConstraintException;
import com.google.common.base.Preconditions;

public class Constraint {
    final private String constraint;

    private Constraint(String constraint) {
        this.constraint = constraint;
    }

    public static class ConstraintBuilder {
        private String constraint;

        /**
         * Constructs a new ConstraintBuilder instance
         */
        public ConstraintBuilder() {}

        /**
         * Sets the constraint description for the constraint being built
         *
         * @param constraint The text description of the constraint
         * @return The ConstraintBuilder instance for method chaining
         * @throws InvalidConstraintException if constraint is null or empty
         */
        public ConstraintBuilder constraint(String constraint) throws InvalidConstraintException {
            if (constraint == null || constraint.isEmpty()) {
                throw new InvalidConstraintException("Constraint should not be null or empty.");
            }
            this.constraint = constraint;
            return this;
        }

        /**
         * Builds and returns a new Constraint instance with the configured properties
         *
         * @return A new Constraint instance
         */
        public Constraint build() {
            return new Constraint(constraint);
        }
    }

    /**
     * Validates the state of the Constraint object
     */
    private void checkConstraint() {
        Preconditions.checkState(constraint != null, "Constraint should not be null.");
        Preconditions.checkState(constraint.length() >= 1, "Constraint should have at least one symbol.");
    }

    public String getConstraint() {
        checkConstraint();
        return constraint;
    }
}