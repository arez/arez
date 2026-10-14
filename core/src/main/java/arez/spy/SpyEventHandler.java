package arez.spy;

/**
 * Interface for receiving spy events.
 */
@FunctionalInterface
public interface SpyEventHandler {
    /**
     * Report an event in the Arez system.
     *
     * @param event the event that occurred.
     */
    void onSpyEvent(Object event);
}
