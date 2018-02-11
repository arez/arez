package arez;

import arez.spy.ObserverInfo;
import arez.spy.TransactionInfo;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import static org.realityforge.braincheck.Guards.*;

/**
 * Adapter of Transaction to TransactionInfo for spy capabilities.
 */
final class TransactionInfoImpl
  implements TransactionInfo
{
  private final Spy _spy;
  @Nonnull
  private final Transaction _transaction;
  @Nullable
  private TransactionInfo _parent;

  TransactionInfoImpl( @Nonnull final Spy spy, @Nonnull final Transaction transaction )
  {
    _spy = Objects.requireNonNull( spy );
    _transaction = Objects.requireNonNull( transaction );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public String getName()
  {
    return getTransaction().getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isReadOnly()
  {
    return Arez.shouldEnforceTransactionType() && TransactionMode.READ_WRITE != getTransaction().getMode();
  }

  /**
   * {@inheritDoc}
   */
  @Nullable
  @Override
  public TransactionInfo getParent()
  {
    if ( null != _parent )
    {
      return _parent;
    }
    else
    {
      final Transaction previous = getTransaction().getPrevious();
      if ( null != previous )
      {
        _parent = new TransactionInfoImpl( _spy, previous );
        return _parent;
      }
      else
      {
        return null;
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTracking()
  {
    return null != getTransaction().getTracker();
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public ObserverInfo getTracker()
  {
    final Observer tracker = getTransaction().getTracker();
    apiInvariant( () -> null != tracker,
                  () -> "Invoked getTracker on TransactionInfo named '" + getName() + "' but no tracker exists." );
    assert null != tracker;
    return new ObserverInfoImpl( _spy, tracker );
  }

  @Nonnull
  Transaction getTransaction()
  {
    return _transaction;
  }
}
