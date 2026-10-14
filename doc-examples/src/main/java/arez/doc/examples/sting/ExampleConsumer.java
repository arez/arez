package arez.doc.examples.sting;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import sting.Injectable;

@NullMarked
@Injectable
public class ExampleConsumer {
    private final @Nullable MyService _myService;

    ExampleConsumer(final @Nullable MyService myService) {
        _myService = myService;
    }

    public void performSomeAction(int value) {
        _myService.performAction(value);
    }
}
