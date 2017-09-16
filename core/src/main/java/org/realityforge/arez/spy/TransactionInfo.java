package org.realityforge.arez.spy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.arez.Observer;

/**
 * Spy interface into transaction data.
 */
public interface TransactionInfo
{
  /**
   * Return the name of the transaction.
   * Transactions usually have the same name as the Action or Observer that triggered transaction.
   *
   * @return the name of the transaction.
   */
  @Nonnull
  String getName();

  /**
   * Return true if the transaciton only allows reads, false if writes allowed.
   *
   * @return true if the transaciton only allows reads, false if writes allowed.
   */
  boolean isReadOnly();

  /**
   * Return the parent transaction if any.
   * The parent transaction was the one that was active when this transaction began.
   *
   * @return the parent transaction if any.
   */
  @Nullable
  TransactionInfo getParent();

  /**
   * Return true if this transaction is tracking observables accessed within the transaction.
   *
   * @return true if this transaction is tracking observables accessed within the transaction.
   */
  boolean isTracking();

  /**
   * Return the tracker associated with this transaction.
   * This method should not be invoked unless {@link #isTracking()} returns true.
   *
   * @return the Observer that is tracker for this transaction.
   */
  @Nonnull
  Observer getTracker();
}
