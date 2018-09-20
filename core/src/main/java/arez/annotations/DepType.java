package arez.annotations;

/**
 * Enumeration that describes the types of dependencies supported by an {@link arez.Observer} or a {@link arez.ComputedValue}.
 */
public enum DepType
{
  /**
   * The reactive element MUST have at least one dependency on an {@link arez.ObservableValue}.
   * This means that the <code>computed</code> or <code>observed</code> method must invoke
   * {@link arez.ObservableValue#reportObserved()} at least once within the scope of the method.
   */
  AREZ,
  /**
   * The reactive element may have dependencies on zero or more {@link arez.ObservableValue} instances.
   * Using this dependency type also allows for the scenario where the <code>computed</code> or
   * <code>observed</code> does not invoke {@link arez.ObservableValue#reportObserved()} within the scope
   * of the method. The <code>computed</code> or <code>observed</code> will not be re-scheduled by the
   * runtime when there are no dependencies. (This is sometimes acceptable, particularly during when a
   * collection of components are being disposed over multiple scheduling rounds).
   */
  AREZ_OR_NONE,
  /**
   * The reactive element may have zero or more dependencies on {@link arez.ObservableValue} instances and/or
   * may have dependencies on non-arez elements. The application can invoke {@link arez.Observer#reportStale()}
   * or {@link arez.ComputedValue#reportPossiblyChanged()} to indicate non-arez dependency has changed.
   */
  AREZ_OR_EXTERNAL
}
