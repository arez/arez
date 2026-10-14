package arez.doc.examples.multi_zone;

import arez.Arez;
import arez.ObservableValue;
import arez.Zone;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class BasicExample {
    // DOC ELIDE START
    private BasicExample() {}
    // DOC ELIDE END
    public static void main(final String @Nullable [] args) throws Throwable {
        // Create zone
        final Zone zone = Arez.createZone();
        // Activate the newly created zone and suspend the current zone if any
        zone.run(() -> {
            // Create new observableValue and ensure it is bound to the current zone
            // The snippet demonstrates creation; use belongs to the application.
            @SuppressWarnings("UnusedVariable")
            final ObservableValue<Object> observableValue = Arez.context().observable();

            // Zone is deactivated when it leaves this block
        });
    }
}
