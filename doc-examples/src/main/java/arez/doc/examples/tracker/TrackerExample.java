package arez.doc.examples.tracker;

import arez.Arez;
import arez.Observer;
import arez.Procedure;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("Convert2MethodRef")
public class TrackerExample {
    // DOC ELIDE START
    private TrackerExample() {}
    // DOC ELIDE END
    public static void main(String @Nullable [] args) throws Throwable {
        final Procedure observedFunction = () -> {
            // Interact with arez observable state (or computable values) here
            // and any time these changed the rescheduleRender function will
            // be run which will somehow reschedule this function.
            // DOC ELIDE START
            // DOC ELIDE END
        };
        final Observer observer = Arez.context().tracker(() -> rescheduleRender());
        // DOC ELIDE START
        // DOC ELIDE END
        // The rescheduleRender should ultimately result in the following
        // invocation. This call will need to be run at least once so that
        // the Arez runtime can determine the dependencies and reschedule
        // when the dependencies are changed.
        Arez.context().observe(observer, observedFunction);
    }

    static void rescheduleRender() {}
}
