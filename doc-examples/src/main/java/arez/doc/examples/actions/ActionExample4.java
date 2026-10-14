package arez.doc.examples.actions;

import arez.ActionFlags;
import arez.Arez;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ActionExample4 {
    public static void main(String @Nullable [] args) throws Throwable {
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
