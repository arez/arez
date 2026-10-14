package arez;

import static org.realityforge.braincheck.Guards.*;

import grim.annotations.OmitSymbol;
import grim.annotations.OmitType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Class supporting the propagation of errors for ObserverErrorHandler callback to multiple error handlers.
 */
@OmitType(unless = "arez.enable_observer_error_handlers")
final class ObserverErrorHandlerSupport implements ObserverErrorHandler {
    /**
     * The list of error handlers to call when an error is received.
     */
    private final List<ObserverErrorHandler> _handlers = new ArrayList<>();

    /**
     * Add error handler to the list of error handlers called.
     * The handler should not already be in the list.
     *
     * @param handler the error handler.
     */
    void addObserverErrorHandler(final ObserverErrorHandler handler) {
        if (Arez.shouldCheckApiInvariants()) {
            apiInvariant(
                    () -> !_handlers.contains(handler),
                    () -> "Arez-0096: Attempting to add handler " + handler + " that is already in "
                            + "the list of error handlers.");
        }
        _handlers.add(Objects.requireNonNull(handler));
    }

    /**
     * Remove error handler from list of existing error handlers.
     * The handler should already be in the list.
     *
     * @param handler the error handler.
     */
    void removeObserverErrorHandler(final ObserverErrorHandler handler) {
        if (Arez.shouldCheckApiInvariants()) {
            apiInvariant(
                    () -> _handlers.contains(handler),
                    () -> "Arez-0097: Attempting to remove handler " + handler + " that is not in "
                            + "the list of error handlers.");
        }
        _handlers.remove(Objects.requireNonNull(handler));
    }

    @Override
    public void onObserverError(
            final Observer observer, final ObserverError error, @Nullable final Throwable throwable) {
        for (final ObserverErrorHandler errorHandler : _handlers) {
            try {
                errorHandler.onObserverError(observer, error, throwable);
            } catch (final Throwable nestedError) {
                if (Arez.areNamesEnabled()) {
                    final String message = ArezUtil.safeGetString(
                            () -> "Exception when notifying error handler '" + errorHandler + "' of '" + error.name()
                                    + "' error in observer named '" + observer.getName() + "'.");
                    ArezLogger.log(message, nestedError);
                } else {
                    ArezLogger.log("Error triggered when invoking ObserverErrorHandler.onObserverError()", nestedError);
                }
            }
        }
    }

    @OmitSymbol
    List<ObserverErrorHandler> getHandlers() {
        return _handlers;
    }
}
