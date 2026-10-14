package arez.doc.examples.reference;

import arez.annotations.ArezComponent;
import arez.component.internal.AbstractRepository;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class PermissionRepository extends AbstractRepository<Integer, Permission, PermissionRepository> {
    // DOC ELIDE START
    // DOC ELIDE END
    @Nullable
    public Permission findById(final int id) {
        return findByArezId(id);
    }
    // DOC ELIDE START
    // DOC ELIDE END
}
