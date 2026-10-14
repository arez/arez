package arez.doc.examples.actions;

import arez.ActionFlags;
import arez.Arez;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ActionExample4 {
    // DOC ELIDE START
    private ActionExample4() {}
    // DOC ELIDE END
    public static void main(String @Nullable [] args) throws Throwable {
        // The snippet demonstrates receiving a result before a caller uses it.
        @SuppressWarnings("UnusedVariable")
        final int result = Arez.context()
                .action(
                        "MyAction",
                        () -> {
                            // Interact with arez observable state (or computable values) here
                            // DOC ELIDE START
                            @SuppressWarnings("UnnecessaryLocalVariable")
                            int value = 0;
                            // DOC ELIDE END
                            return value;
                        },
                        ActionFlags.READ_ONLY);
    }
}
