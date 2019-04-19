package arez;

import arez.spy.ObserverInfo;
import arez.spy.TransactionInfo;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static arez.Guards.*;

/**
 * Adapter of Transaction to TransactionInfo for spy capabilities.
 */
final class TransactionInfoImpl
  implements TransactionInfo
{
  @Nonnull
  private final Transaction _transaction;

  TransactionInfoImpl( @Nonnull final Transaction transaction )
  {
    _transaction = Objects.requireNonNull( transaction );
  }

  @Nonnull
  @Override
  public String getName()
  {
    return getTransaction().getName();
  }

  @Override
  public boolean isReadOnly()
  {
    return Arez.shouldEnforceTransactionType() && !getTransaction().isMutation();
  }

  @Nullable
  @Override
  public TransactionInfo getParent()
  {
    final Transaction previous = _transaction.getPrevious();
    return null != previous ? previous.asInfo() : null;
  }

  @Override
  public boolean isTracking()
  {
    return null != getTransaction().getTracker();
  }

  @Nonnull
  @Override
  public ObserverInfo getTracker()
  {
    final Observer tracker = getTransaction().getTracker();
    apiInvariant( () -> null != tracker,
                  () -> "Invoked getTracker on TransactionInfo named '" + getName() + "' but no tracker exists." );
    assert null != tracker;
    return tracker.asInfo();
  }

  @Nonnull
  Transaction getTransaction()
  {
    return _transaction;
  }
}
