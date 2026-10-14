package arez.doc.examples.sting;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StingExample {
    // DOC ELIDE START
    private StingExample() {}
    // DOC ELIDE END
    public static void main(String @Nullable [] args) {
        final var component = ExampleStingInjector.create();
        component.getExampleConsumer().performSomeAction(42);
    }
}
