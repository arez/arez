package arez.doc.examples.sting;

import org.jspecify.annotations.NullMarked;
import sting.Injector;

@NullMarked
@Injector(includes = MyService.class, fragmentOnly = false)
public interface ExampleStingInjector {
    static ExampleStingInjector create() {
        return new Sting_ExampleStingInjector();
    }

    ExampleConsumer getExampleConsumer();
}
