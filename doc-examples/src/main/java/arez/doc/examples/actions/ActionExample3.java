package arez.doc.examples.actions;

import arez.Arez;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ActionExample3 {
    public static void main(String @Nullable [] args) {
        final int result = Arez.context().safeAction(() -> {
            // Interact with arez observable state (or computable values) here
            // DOC ELIDE START
            @SuppressWarnings("UnnecessaryLocalVariable")
            int value = 0;
            // DOC ELIDE END
            return value;
        });
    }
}
