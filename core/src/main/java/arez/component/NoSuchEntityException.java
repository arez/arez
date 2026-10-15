package arez.component;

import arez.Arez;

/**
 * Exception thrown by repository when the query for a specific entity failed.
 */
public class NoSuchEntityException extends NoResultException {

    private final Object _id;

    /**
     * Create the exception
     *
     * @param id the id of the entity that was queried.
     */
    public NoSuchEntityException(final Object id) {
        _id = id;
    }

    /**
     * Return the id of the entity that was not found.
     *
     * @return the id of the entity that was not found.
     */
    public Object getId() {
        return _id;
    }

    @Override
    public String toString() {
        if (Arez.areNamesEnabled()) {
            return "NoSuchEntityException[id=" + _id + ']';
        } else {
            return super.toString();
        }
    }
}
