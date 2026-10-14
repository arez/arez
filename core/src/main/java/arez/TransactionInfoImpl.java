package arez;

import static org.realityforge.braincheck.Guards.*;

import arez.spy.ObserverInfo;
import arez.spy.TransactionInfo;
import grim.annotations.OmitType;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Adapter of Transaction to TransactionInfo for spy capabilities.
 */
@OmitType(unless = "arez.enable_spies")
final class TransactionInfoImpl implements TransactionInfo {

    private final Transaction _transaction;

    TransactionInfoImpl(final Transaction transaction) {
        _transaction = Objects.requireNonNull(transaction);
    }

    @Override
    public String getName() {
        return getTransaction().getName();
    }

    @Override
    public boolean isReadOnly() {
        return Arez.shouldEnforceTransactionType() && !getTransaction().isMutation();
    }

    @Nullable
    @Override
    public TransactionInfo getParent() {
        final Transaction previous = _transaction.getPrevious();
        return null != previous ? previous.asInfo() : null;
    }

    @Override
    public boolean isTracking() {
        return null != getTransaction().getTracker();
    }

    @Override
    public ObserverInfo getTracker() {
        final Observer tracker = getTransaction().getTracker();
        apiInvariant(
                () -> null != tracker,
                () -> "Invoked getTracker on TransactionInfo named '" + getName() + "' but no tracker exists.");
        assert null != tracker;
        return tracker.asInfo();
    }

    Transaction getTransaction() {
        return _transaction;
    }
}
