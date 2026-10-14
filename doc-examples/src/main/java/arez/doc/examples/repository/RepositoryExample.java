package arez.doc.examples.repository;

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

        assert repository.findById(-1) == null;
        assert repository.findById(component.getId()) == component;
        assert repository.getById(component.getId()) == component;
        assert repository.findAll().size() == 1;
        assert repository.findByQuery(c -> c.getName().equals("MyName")) == component;
        assert repository.findByQuery(c -> c.getName().equals("NotMyName")) == null;
        // EXAMPLE END
    }
}
