package arez.spy;

import arez.Arez;
import arez.ComputableValue;
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A representation of a observer instance exposed to spy framework.
 */
public interface ObserverInfo extends ElementInfo {
    /**
     * Return true if the Observer is active.
     * An observer is inactive before its first observation, after suspension has released its
     * tracking resources, and after disposal. Explicit observations while suspended are temporarily
     * active until tracking completes. For a {@link ComputableValue}, activity follows observation
     * and keep-alive requirements.
     *
     * @return true if the Observer is active.
     */
    boolean isActive();

    /**
     * Return true if the Observer is currently running.
     *
     * @return true if the Observer is currently running.
     */
    boolean isRunning();

    /**
     * Return true if the Observer is scheduled to run.
     *
     * @return true if the Observer is scheduled to run.
     */
    boolean isScheduled();

    /**
     * Return true if the Observer is a ComputableValue.
     *
     * @return true if the Observer is a ComputableValue.
     */
    boolean isComputableValue();

    /**
     * Return true if the Observer will use a read-only transaction.
     *
     * @return true if the Observer will use a read-only transaction.
     */
    boolean isReadOnly();

    /**
     * Return the priority of the Observer.
     *
     * @return the priority of the Observer.
     */
    Priority getPriority();

    /**
     * Convert the Observer to a ComputableValue.
     * This method should only be called if {@link #isComputableValue()} returns true.
     *
     * @return the ComputableValue instance.
     */
    ComputableValueInfo asComputableValue();

    /**
     * Return the list of dependencies of the Observer.
     * The list is an immutable copy of the dependencies of the {@link arez.Observer}.
     * If the {@link arez.Observer} is currently running (i.e. {@link #isRunning()}
     * returns true) then the dependencies are provisional and may be added to as transaction
     * completes.
     *
     * @return the list of dependencies for the Observer.
     */
    List<ObservableValueInfo> getDependencies();

    /**
     * Return the component for the specified Observer.
     * This method should not be invoked if {@link Arez#areNativeComponentsEnabled()} returns false.
     *
     * @return the component that contains Observer if any.
     */
    @Nullable
    ComponentInfo getComponent();
}
