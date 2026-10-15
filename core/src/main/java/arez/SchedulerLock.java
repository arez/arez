package arez;

import static org.realityforge.braincheck.Guards.*;

import grim.annotations.OmitSymbol;
import org.jspecify.annotations.Nullable;

/**
 * A lock that stops the scheduler from running tasks until the lock is released.
 */
public final class SchedulerLock implements Disposable {
    @OmitSymbol(unless = "arez.enable_zones")
    @Nullable
    private final ArezContext _context;
    /**
     * True if already released.
     */
    private boolean _released;

    SchedulerLock(@Nullable final ArezContext context) {
        if (Arez.shouldCheckInvariants()) {
            invariant(
                    () -> Arez.areZonesEnabled() || null == context,
                    () -> "Arez-0174: SchedulerLock passed a context but Arez.areZonesEnabled() is false");
        }
        assert !Arez.areZonesEnabled() || null != context;
        _context = Arez.areZonesEnabled() ? context : null;
    }

    @Override
    public void dispose() {
        if (isNotDisposed()) {
            _released = true;
            getContext().releaseSchedulerLock();
        }
    }

    @Override
    public boolean isDisposed() {
        return _released;
    }

    private ArezContext getContext() {
        if (Arez.areZonesEnabled()) {
            assert null != _context;
            return _context;
        } else {
            return Arez.context();
        }
    }
}
