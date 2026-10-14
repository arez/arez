package arez.doc.examples.actions;

import arez.Arez;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ActionExample {
    // DOC ELIDE START
    private ActionExample() {}
    // DOC ELIDE END
    public static void main(String @Nullable [] args) throws Throwable {
        Arez.context().action(() -> {
            // Interact with arez observable state (or computable values) here
            // DOC ELIDE START
            // DOC ELIDE END
        });
    }
}
