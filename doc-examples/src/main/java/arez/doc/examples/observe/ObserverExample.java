package arez.doc.examples.observe;

import arez.Arez;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ObserverExample {
    public static void main(String @Nullable [] args) {
        Arez.context().observer(() -> {
            // Interact with arez observable state (or computable values) here
            // and any time these changed this function will be re-run.
            // DOC ELIDE START
            // DOC ELIDE END
        });
    }
}
