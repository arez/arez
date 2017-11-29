package org.realityforge.arez;

import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.anodoc.Unsupported;
import org.realityforge.arez.spy.ComponentInfo;
import org.realityforge.arez.spy.ObserverInfo;
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
  boolean isComputing( @Nonnull ComputedValue<?> computedValue );

  /**
   * Return true if the ComputedValue is active.
   * A ComputedValue is active if there is one or more Observers and the value will be calculated.
   *
   * @param computedValue the ComputedValue.
   * @return true if the ComputedValue is active.
   */
  boolean isActive( @Nonnull ComputedValue<?> computedValue );

  /**
   * Return the list of observers for ComputedValue.
   * The list is an immutable copy of the observers of the {@link ComputedValue}.
   *
   * @param computedValue the ComputedValue.
   * @return the list of observers for ComputedValue.
   */
  @Nonnull
  List<Observer> getObservers( @Nonnull ComputedValue<?> computedValue );

  /**
   * Return the list of dependencies of the ComputedValue.
   * The list is an immutable copy of the dependencies of the {@link ComputedValue}.
   * If the {@link ComputedValue} is currently being computed (i.e. {@link #isComputing(ComputedValue)}
   * returns true) then the dependencies are provisional and may be added to as transaction
   * completes.
   *
   * @param computedValue the ComputedValue.
   * @return the list of dependencies for ComputedValue.
   */
  @Nonnull
  List<Observable> getDependencies( @Nonnull ComputedValue<?> computedValue );

  /**
   * Return true if the Observable is a ComputedValue.
   *
   * @param observable the Observable.
   * @return true if the Observable is a ComputedValue.
   */
  boolean isComputedValue( @Nonnull Observable<?> observable );

  /**
   * Convert the Observable to a ComputedValue.
   * This method should only be called if {@link #isComputedValue(Observable)} returns true.
   *
   * @param observable the Observable.
   * @return the ComputedValue instance.
   */
  ComputedValue<?> asComputedValue( @Nonnull Observable<?> observable );

  /**
   * Return the list of observers for the Observable.
   * The list is an immutable copy of the observers of the {@link Observable}.
   *
   * @param observable the Observable.
   * @return the list of observers for Observable.
   */
  @Nonnull
  List<Observer> getObservers( @Nonnull Observable<?> observable );

  /**
   * Return true if the Observer is currently running.
   *
   * @param observer the Observer.
   * @return true if the Observer is currently running.
   * @see org.realityforge.arez.spy.ObserverInfo#isRunning()
   */
  boolean isRunning( @Nonnull Observer observer );

  /**
   * Return true if the Observer is scheduled to run.
   *
   * @param observer the Observer.
   * @return true if the Observer is scheduled to run.
   * @see org.realityforge.arez.spy.ObserverInfo#isScheduled()
   */
  boolean isScheduled( @Nonnull Observer observer );

  /**
   * Return true if the Observer is a ComputedValue.
   *
   * @param observer the Observer.
   * @return true if the Observer is a ComputedValue.
   * @see org.realityforge.arez.spy.ObserverInfo#isComputedValue()
   */
  boolean isComputedValue( @Nonnull Observer observer );

  /**
   * Return true if the Observer will use a read-only transaction.
   *
   * @param observer the Observer.
   * @return true if the Observer will use a read-only transaction.
   * @see org.realityforge.arez.spy.ObserverInfo#isReadOnly()
   */
  boolean isReadOnly( @Nonnull Observer observer );

  /**
   * Convert the Observer to a ComputedValue.
   * This method should only be called if {@link #isComputedValue(Observer)} returns true.
   *
   * @param observer the Observer.
   * @return the ComputedValue instance.
   * @see org.realityforge.arez.spy.ObserverInfo#asComputedValue()
   */
  ComputedValue<?> asComputedValue( @Nonnull Observer observer );

  /**
   * Return the list of dependencies of the Observer.
   * The list is an immutable copy of the dependencies of the {@link Observer}.
   * If the {@link Observer} is currently running (i.e. {@link #isRunning(Observer)}
   * returns true) then the dependencies are provisional and may be added to as transaction
   * completes.
   *
   * @param observer the Observer.
   * @return the list of dependencies for the Observer.
   * @see org.realityforge.arez.spy.ObserverInfo#getDependencies()
   */
  @Nonnull
  List<Observable<?>> getDependencies( @Nonnull Observer observer );

  /**
   * Return the component for specified Observable.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @param observable the Observable.
   * @return the component that contains Observable if any.
   */
  @Nullable
  ComponentInfo getComponent( @Nonnull Observable<?> observable );

  /**
   * Return the component for specified Observer.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @param observer the Observer.
   * @return the component that contains Observer if any.
   * @see org.realityforge.arez.spy.ObserverInfo#getComponent()
   */
  @Nullable
  ComponentInfo getComponent( @Nonnull Observer observer );

  /**
   * Return the component for specified ComputedValue.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @param computedValue the ComputedValue.
   * @return the component that contains ComputedValue if any.
   */
  @Nullable
  ComponentInfo getComponent( @Nonnull ComputedValue<?> computedValue );

  /**
   * Find the component identified by the specified type and id.
   *
   * @param type the component type.
   * @param id   the component id. Should be null if the component is a singleton.
   * @return the component descriptor matching the specified type and id.
   */
  @Nullable
  ComponentInfo findComponent( @Nonnull String type, @Nonnull Object id );

  /**
   * Find all the components identified by the specified type.
   * This collection returned is unmodifiable.
   *
   * @param type the component type.
   * @return the collection of component descriptors of specified type.
   */
  @Nonnull
  Collection<ComponentInfo> findAllComponentsByType( @Nonnull String type );

  /**
   * Find all the component types in the system.
   * This is essentially all the types that have at least 1 instance.
   * This collection returned is unmodifiable.
   *
   * @return the collection of component types.
   */
  @Nonnull
  Collection<String> findAllComponentTypes();

  /**
   * Find all the collection of observables not contained by a native component.
   * This method should not be invoked unless {@link Arez#areRegistriesEnabled()} returns true.
   * This collection returned is unmodifiable.
   *
   * @return the collection of observables not contained by a native component.
   */
  @Nonnull
  Collection<Observable<?>> findAllTopLevelObservables();

  /**
   * Find all the collection of observers not contained by a native component.
   * This method should not be invoked unless {@link Arez#areRegistriesEnabled()} returns true.
   * This collection returned is unmodifiable.
   *
   * @return the collection of observers not contained by a native component.
   */
  @Nonnull
  Collection<ObserverInfo> findAllTopLevelObservers();

  /**
   * Find all the collection of computed values not contained by a native component.
   * This method should not be invoked unless {@link Arez#areRegistriesEnabled()} returns true.
   * This collection returned is unmodifiable.
   *
   * @return the collection of computed values not contained by a native component.
   */
  @Nonnull
  Collection<ComputedValue<?>> findAllTopLevelComputedValues();

  /**
   * Return true if the specified Observable has an accessor.
   * This method should not be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns false.
   *
   * @param observable the Observable.
   * @return true if an accessor is available.
   */
  <T> boolean hasAccessor( @Nonnull Observable<T> observable );

  /**
   * Return the value of the specified Observable.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true
   * and {@link #hasAccessor(Observable)} for the same element returns true.
   *
   * @param observable the Observable.
   * @return the value of the observable.
   * @throws Throwable if the property accessor throws an exception.
   */
  @Nullable
  <T> T getValue( @Nonnull Observable<T> observable )
    throws Throwable;

  /**
   * Return true if the specified Observable has a mutator.
   * This method should not be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns false.
   *
   * @param observable the Observable.
   * @return true if a mutator is available.
   */
  <T> boolean hasMutator( @Nonnull Observable<T> observable );

  /**
   * Set the value of the specified Observable.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true
   * and {@link #hasMutator(Observable)} for the same element returns true.
   *
   * @param observable the Observable.
   * @param value      the value to set
   * @throws Throwable if the property accessor throws an exception.
   */
  <T> void setValue( @Nonnull Observable<T> observable, @Nullable T value )
    throws Throwable;

  /**
   * Return the value of the specified ComputedValue.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true.
   *
   * @param computedValue the ComputedValue.
   * @return the value of the ComputedValue.
   * @throws Throwable if the property accessor throws an exception.
   */
  @Nullable
  <T> T getValue( @Nonnull ComputedValue<T> computedValue )
    throws Throwable;
}
