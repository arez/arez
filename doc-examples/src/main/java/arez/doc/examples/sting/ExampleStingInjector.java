package arez.doc.examples.sting;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import sting.Injector;

@NullMarked
@Injector(includes = MyService.class, fragmentOnly = false)
public interface ExampleStingInjector {
    static @Nullable ExampleStingInjector create() {
        return new Sting_ExampleStingInjector();
    }

    @Nullable
    ExampleConsumer getExampleConsumer();
}
