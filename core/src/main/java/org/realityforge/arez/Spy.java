package org.realityforge.arez;

import java.util.List;
import javax.annotation.Nonnull;
import org.realityforge.arez.spy.TransactionInfo;

/**
 * Interface for interacting with spy system.
 */
@Unsupported( "This services is primary here to support DevTools and will evolve as requirements dictate" )
public interface Spy
{
  /**
   * Add a spy handler to the list of handlers.
   * The handler should not already be in the list.
   *
   * @param handler the spy handler.
   */
  void addSpyEventHandler( @Nonnull SpyEventHandler handler );

  /**
   * Remove spy handler from list of existing handlers.
   * The handler should already be in the list.
   *
   * @param handler the spy handler.
   */
  void removeSpyEventHandler( @Nonnull SpyEventHandler handler );

  /**
   * Return true if spy events will be propagated.
   * This means spies are enabled and there is at least one spy event handler present.
   *
   * @return true if spy events will be propagated, false otherwise.
   */
  boolean willPropagateSpyEvents();

  /**
   * Report an event in the Arez system.
   *
   * @param event the event that occurred.
   */
  void reportSpyEvent( @Nonnull Object event );

  /**
   * Return true if there is a transaction active.
   *
   * @return true if there is a transaction active.
   */
  boolean isTransactionActive();

  /**
   * Return the current transaction.
   * This method should not be invoked unless {@link #isTransactionActive()} returns true.
   *
   * @return the current transaction.
   */
  @Nonnull
  TransactionInfo getTransaction();

  /**
   * Return true if the specified ComputedValue is "computing".
   * This implies that the current transaction or one of the parent transactions is calculating the
   * ComputedValue at the moment.
   *
   * @param computedValue the ComputedValue.
   * @return true if there is a transaction active.
   */
  boolean isComputing( @Nonnull final ComputedValue<?> computedValue );

  /**
   * Return true if the ComputedValue is active.
   * A ComputedValue is active if there is one or more Observers and the value will be calculated.
   *
   * @param computedValue the ComputedValue.
   * @return true if the ComputedValue is active.
   */
  boolean isActive( @Nonnull final ComputedValue<?> computedValue );

  /**
   * Return the list of observers for ComputedValue.
   * The list is an immutable copy of the observers of the {@link ComputedValue}.
   *
   * @param computedValue the ComputedValue.
   * @return the list of observers for ComputedValue.
   */
  @Nonnull
  List<Observer> getObservers( @Nonnull final ComputedValue<?> computedValue );

  /**
   * Return the list of dependencies of the ComputedValue.
   * The list is an immutable copy of the dependencies of the {@link ComputedValue}.
   * If the {@link ComputedValue} is currently being computed (i.e. {@link #isComputing(ComputedValue)}
   * returns true) then the dependencies are provisional and
   *
   * @param computedValue the ComputedValue.
   * @return the list of dependencies for ComputedValue.
   */
  @Nonnull
  List<Observable> getDependencies( @Nonnull final ComputedValue<?> computedValue );

  /**
   * Return true if the Observable is a ComputedValue.
   *
   * @param observable the Observable.
   * @return true if the Observable is a ComputedValue.
   */
  boolean isComputedValue( @Nonnull final Observable observable );

  /**
   * Convert the Observable to a ComputedValue.
   * This method should only be called if {@link #isComputedValue(Observable)} returns true.
   *
   * @param observable the Observable.
   * @return the ComputedValue instance.
   */
  ComputedValue<?> asComputedValue( @Nonnull final Observable observable );

  /**
   * Return the list of observers for the Observable.
   * The list is an immutable copy of the observers of the {@link Observable}.
   *
   * @param observable the Observable.
   * @return the list of observers for Observable.
   */
  @Nonnull
  List<Observer> getObservers( @Nonnull final Observable observable );

  /**
   * Return true if the Observer is scheduled to run.
   *
   * @param observer the Observer.
   * @return true if the Observer is scheduled to run.
   */
  boolean isScheduled( @Nonnull final Observer observer );

  /**
   * Return true if the Observable is a ComputedValue.
   *
   * @param observer the Observer.
   * @return true if the Observer is a ComputedValue.
   */
  boolean isComputedValue( @Nonnull final Observer observer );

  /**
   * Convert the Observer to a ComputedValue.
   * This method should only be called if {@link #isComputedValue(Observer)} returns true.
   *
   * @param observer the Observer.
   * @return the ComputedValue instance.
   */
  ComputedValue<?> asComputedValue( @Nonnull final Observer observer );
}
