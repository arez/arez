package arez;

import arez.spy.ComponentInfo;
import arez.spy.ComputedValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import arez.spy.TransactionInfo;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Interface for interacting with spy system.
 */
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
  List<ObserverInfo> getObservers( @Nonnull ComputedValue<?> computedValue );

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
  List<ObservableValueInfo> getDependencies( @Nonnull ComputedValue<?> computedValue );

  /**
   * Return true if the ObservableValue is a ComputedValue.
   *
   * @param observableValue the ObservableValue.
   * @return true if the ObservableValue is a ComputedValue.
   */
  boolean isComputedValue( @Nonnull ObservableValue<?> observableValue );

  /**
   * Convert the ObservableValue to a ComputedValue.
   * This method should only be called if {@link #isComputedValue(ObservableValue)} returns true.
   *
   * @param observableValue the ObservableValue.
   * @return the ComputedValue instance.
   */
  ComputedValueInfo asComputedValue( @Nonnull ObservableValue<?> observableValue );

  /**
   * Return true if the Observer is a ComputedValue.
   *
   * @param observer the Observer.
   * @return true if the Observer is a ComputedValue.
   * @see arez.spy.ObserverInfo#isComputedValue()
   */
  boolean isComputedValue( @Nonnull Observer observer );

  /**
   * Return the component for specified ObservableValue.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @param observableValue the ObservableValue.
   * @return the component that contains ObservableValue if any.
   */
  @Nullable
  ComponentInfo getComponent( @Nonnull ObservableValue<?> observableValue );

  /**
   * Return the component for specified Observer.
   * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
   *
   * @param observer the Observer.
   * @return the component that contains Observer if any.
   * @see arez.spy.ObserverInfo#getComponent()
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
  Collection<ObservableValueInfo> findAllTopLevelObservableValues();

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
  Collection<ComputedValueInfo> findAllTopLevelComputedValues();

  /**
   * Return true if the specified ObservableValue has an accessor.
   * This method should not be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns false.
   *
   * @param <T>             The type of the value that is observableValue.
   * @param observableValue the ObservableValue.
   * @return true if an accessor is available.
   */
  <T> boolean hasAccessor( @Nonnull ObservableValue<T> observableValue );

  /**
   * Return the value of the specified ObservableValue.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true
   * and {@link #hasAccessor(ObservableValue)} for the same element returns true.
   *
   * @param <T>             The type of the value that is observableValue.
   * @param observableValue the ObservableValue.
   * @return the value of the observableValue.
   * @throws Throwable if the property accessor throws an exception.
   */
  @Nullable
  <T> T getValue( @Nonnull ObservableValue<T> observableValue )
    throws Throwable;

  /**
   * Return true if the specified ObservableValue has a mutator.
   * This method should not be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns false.
   *
   * @param <T>             The type of the value that is observableValue.
   * @param observableValue the ObservableValue.
   * @return true if a mutator is available.
   */
  <T> boolean hasMutator( @Nonnull ObservableValue<T> observableValue );

  /**
   * Set the value of the specified ObservableValue.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true
   * and {@link #hasMutator(ObservableValue)} for the same element returns true.
   *
   * @param <T>             The type of the value that is observableValue.
   * @param observableValue the ObservableValue.
   * @param value           the value to set
   * @throws Throwable if the property accessor throws an exception.
   */
  <T> void setValue( @Nonnull ObservableValue<T> observableValue, @Nullable T value )
    throws Throwable;

  /**
   * Return the value of the specified ComputedValue.
   * This method should only be invoked if {@link Arez#arePropertyIntrospectorsEnabled()} returns true.
   *
   * @param <T>           The type of the value that is computed.
   * @param computedValue the ComputedValue.
   * @return the value of the ComputedValue.
   * @throws Throwable if the property accessor throws an exception.
   */
  @Nullable
  <T> T getValue( @Nonnull ComputedValue<T> computedValue )
    throws Throwable;

  /**
   * Convert the specified component into an ComponentInfo.
   *
   * @param component the Component.
   * @return the ComponentInfo.
   */
  @Nonnull
  ComponentInfo asComponentInfo( @Nonnull Component component );

  /**
   * Convert the specified observer into an ObserverInfo.
   *
   * @param observer the Observer.
   * @return the ObserverInfo.
   */
  @Nonnull
  ObserverInfo asObserverInfo( @Nonnull Observer observer );

  /**
   * Convert the specified observableValue into an ObservableValueInfo.
   *
   * @param <T>             The type of the value that is observableValue.
   * @param observableValue the ObservableValue.
   * @return the ObservableValueInfo wrapping observableValue.
   */
  @Nonnull
  <T> ObservableValueInfo asObservableValueInfo( @Nonnull ObservableValue<T> observableValue );

  /**
   * Convert the specified computedValue into an ComputedValueInfo.
   *
   * @param <T>           The type of the value that is computed.
   * @param computedValue the ComputedValue.
   * @return the ComputedValueInfo wrapping the computedValue.
   */
  @Nonnull
  <T> ComputedValueInfo asComputedValueInfo( @Nonnull ComputedValue<T> computedValue );
}
