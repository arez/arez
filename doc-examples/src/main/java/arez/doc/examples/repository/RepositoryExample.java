package arez.doc.examples.repository;

import arez.Arez;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class RepositoryExample {
    // DOC ELIDE START
    private RepositoryExample() {}
    // DOC ELIDE END
    public static void main(String @Nullable [] args) {
        // EXAMPLE START
        final var repository = MyComponentRepository.newRepository();
        final int myComponentId = 22;
        final MyComponent component = repository.create(myComponentId, "MyName");

        Arez.context().safeAction(() -> {
            assert repository.findById(-1) == null;
            assert repository.findById(component.getId()) == component;
            assert repository.getById(component.getId()) == component;
            assert repository.findAll().size() == 1;
            assert repository.findByName("MyName") == component;
            assert repository.findByName("NotMyName") == null;
        });
        // EXAMPLE END
    }
}
