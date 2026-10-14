package arez.doc.examples.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponentLike;
import arez.component.NoResultException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponentLike
public class MyComponentRepository {

    public static MyComponentRepository newRepository() {
        //// DOC ELIDE START
        //noinspection ConstantConditions
        return null;
        //// DOC ELIDE END
    }

    public MyComponent create(final int id, final String name) {
        //// DOC ELIDE START
        //noinspection ConstantConditions
        return null;
        //// DOC ELIDE END
    }

    public boolean contains(final MyComponent entity) {
        //// DOC ELIDE START
        return true;
        //// DOC ELIDE END
    }

    @Action
    public void destroy(final MyComponent entity) {
        //// DOC ELIDE START
        //// DOC ELIDE END
    }

    public final List<MyComponent> findAll() {
        //// DOC ELIDE START
        //noinspection ConstantConditions
        return null;
        //// DOC ELIDE END
    }

    public final List<MyComponent> findAll(final Comparator<MyComponent> sorter) {
        //// DOC ELIDE START
        //noinspection ConstantConditions
        return null;
        //// DOC ELIDE END
    }

    public final List<MyComponent> findAllByQuery(final Predicate<MyComponent> query) {
        //// DOC ELIDE START
        //noinspection ConstantConditions
        return null;
        //// DOC ELIDE END
    }

    public final List<MyComponent> findAllByQuery(
            final Predicate<MyComponent> query, final Comparator<MyComponent> sorter) {
        //// DOC ELIDE START
        //noinspection ConstantConditions
        return null;
        //// DOC ELIDE END
    }

    @Nullable
    public final MyComponent findByQuery(final Predicate<MyComponent> query) {
        //// DOC ELIDE START
        return null;
        //// DOC ELIDE END
    }

    public final MyComponent getByQuery(final Predicate<MyComponent> query) throws NoResultException {
        //// DOC ELIDE START
        //noinspection ConstantConditions
        return null;
        //// DOC ELIDE END
    }

    @Nullable
    public MyComponent findById(final int id) {
        //// DOC ELIDE START
        return null;
        //// DOC ELIDE END
    }

    public final MyComponent getById(final int id) {
        //// DOC ELIDE START
        //noinspection ConstantConditions
        return null;
        //// DOC ELIDE END
    }

    public final MyComponentRepository self() {
        return this;
    }
}
