package arez.spy;

import arez.Arez;
import arez.Component;
import arez.ComputableValue;
import arez.ObservableValue;
import arez.Observer;
import arez.Task;
import java.util.Collection;
import org.jspecify.annotations.Nullable;

/**
 * Interface for interacting with spy system.
 */
public interface Spy {
    /**
     * Add a spy handler to the list of handlers.
     * The handler should not already be in the list.
     *
     * @param handler the spy handler.
     */
    void addSpyEventHandler(SpyEventHandler handler);

    /**
     * Remove spy handler from list of existing handlers.
     * The handler should already be in the list.
     *
     * @param handler the spy handler.
     */
    void removeSpyEventHandler(SpyEventHandler handler);

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
    void reportSpyEvent(Object event);

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
    TransactionInfo getTransaction();

    /**
     * Find the component identified by the specified type and id.
     *
     * @param type the component type.
     * @param id   the component id. Should be null if the component is a singleton.
     * @return the component descriptor matching the specified type and id.
     */
    @Nullable
    ComponentInfo findComponent(String type, Object id);

    /**
     * Find all the components identified by the specified type.
     * This collection returned is unmodifiable.
     *
     * @param type the component type.
     * @return the collection of component descriptors of specified type.
     */
    Collection<ComponentInfo> findAllComponentsByType(String type);

    /**
     * Find all the component types in the system.
     * This is essentially all the types that have at least 1 instance.
     * This collection returned is unmodifiable.
     *
     * @return the collection of component types.
     */
    Collection<String> findAllComponentTypes();

    /**
     * Find all the observables not contained by a native component.
     * This method should not be invoked unless {@link Arez#areRegistriesEnabled()} returns true.
     * This collection returned is unmodifiable.
     *
     * @return the collection of observables not contained by a native component.
     */
    Collection<ObservableValueInfo> findAllTopLevelObservableValues();

    /**
     * Find all the observers not contained by a native component.
     * This method should not be invoked unless {@link Arez#areRegistriesEnabled()} returns true.
     * This collection returned is unmodifiable.
     *
     * @return the collection of observers not contained by a native component.
     */
    Collection<ObserverInfo> findAllTopLevelObservers();

    /**
     * Find all the computable values not contained by a native component.
     * This method should not be invoked unless {@link Arez#areRegistriesEnabled()} returns true.
     * This collection returned is unmodifiable.
     *
     * @return the collection of computable values not contained by a native component.
     */
    Collection<ComputableValueInfo> findAllTopLevelComputableValues();

    /**
     * Find all the "top-level" tasks defined by the system.
     * This does not return tasks that are used to define Observers etc.
     * This method should not be invoked unless {@link Arez#areRegistriesEnabled()} returns true.
     * This collection returned is unmodifiable.
     *
     * @return the collection of tasks defined by the context.
     */
    Collection<TaskInfo> findAllTopLevelTasks();

    /**
     * Convert the specified component into an ComponentInfo.
     *
     * @param component the Component.
     * @return the ComponentInfo.
     */
    ComponentInfo asComponentInfo(Component component);

    /**
     * Convert the specified observer into an ObserverInfo.
     *
     * @param observer the Observer.
     * @return the ObserverInfo.
     */
    ObserverInfo asObserverInfo(Observer observer);

    /**
     * Convert the specified observableValue into an ObservableValueInfo.
     *
     * @param <T>             The type of the value that is observableValue.
     * @param observableValue the ObservableValue.
     * @return the ObservableValueInfo wrapping observableValue.
     */
    <T extends @Nullable Object> ObservableValueInfo asObservableValueInfo(ObservableValue<T> observableValue);

    /**
     * Convert the specified ComputableValue into an ComputableValueInfo.
     *
     * @param <T>             The type of the value that is computable.
     * @param computableValue the ComputableValue.
     * @return the ComputableValueInfo wrapping the ComputableValue.
     */
    <T extends @Nullable Object> ComputableValueInfo asComputableValueInfo(ComputableValue<T> computableValue);

    /**
     * Convert the specified task into an TaskInfo.
     *
     * @param task the Task.
     * @return the TaskInfo.
     */
    TaskInfo asTaskInfo(Task task);
}
