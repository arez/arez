package arez.doc.examples.sting;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StingExample {
    public static void main(String @Nullable [] args) {
        final ExampleStingInjector component = ExampleStingInjector.create();
        component.getExampleConsumer().performSomeAction(42);
    }
}
