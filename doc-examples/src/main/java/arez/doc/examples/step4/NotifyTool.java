package arez.doc.examples.step4;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class NotifyTool {
    private NotifyTool() {}

    static void notifyUserTicketExpired(@Nullable TrainTicket ticket) {
        // ignored.
    }
}
