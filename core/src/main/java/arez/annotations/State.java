package arez.annotations;

/**
 * Defines the states of a component.
 */
public enum State
{
  /**
   * The reactive elements have been created (i.e. the {@link arez.ObservableValue}, {@link arez.Observer},
   * {@link arez.ComputableValue} etc.). The {@link PostConstruct} method has NOT been invoked.
   */
  CONSTRUCTED,
  /**
   * The {@link PostConstruct} method (if any) has been invoked and {@link arez.Observer}s have been scheduled
   * but the scheduler has not been triggered.
   */
  COMPLETE,
  /**
   * The scheduler has been triggered and any {@link Observe} methods have been invoked if runtime managed.
   */
  READY,
  /**
   * The component is disposing.
   */
  DISPOSING
}
