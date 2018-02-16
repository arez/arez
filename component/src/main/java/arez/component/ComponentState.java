package arez.component;

import javax.annotation.Nonnull;

/**
 * This class is used in generated code to manage the state of the component.
 * This is centralized here so that it is easier for GWT to optimize it either by sharing the methods
 * implementation or inlining as appropriate.
 */
public final class ComponentState
{
  /**
   * The component has been created, but not yet initialized.
   */
  public final static byte COMPONENT_CREATED = 0;
  /**
   * The components constructor has been called, the {@link arez.ArezContext} field initialized (if necessary),
   * and the synthetic id has been generated (if required).
   */
  public final static byte COMPONENT_INITIALIZED = 1;
  /**
   * The reactive elements have been created (i.e. the {@link arez.Observable}, {@link arez.Observer},
   * {@link arez.ComputedValue} etc.), the {@link arez.annotations.PostConstruct} has been invoked and
   * the {@link arez.Component} has been instantiated. The scheduler has not been triggered.
   */
  public final static byte COMPONENT_CONSTRUCTED = 2;
  /**
   * The scheduler has been triggered and any {@link arez.annotations.Autorun} methods have been invoked
   * or scheduled.
   */
  public final static byte COMPONENT_READY = 3;
  /**
   * The component is disposing.
   */
  public final static byte COMPONENT_DISPOSING = -2;
  /**
   * The component has been disposed.
   */
  public final static byte COMPONENT_DISPOSED = -1;

  /**
   * Return true if the component has been initialized.
   *
   * @param state the component state.
   * @return true if the component has been initialized.
   */
  public static boolean hasBeenInitialized( final byte state )
  {
    return COMPONENT_CREATED != state;
  }

  /**
   * Return true if the component has been constructed.
   *
   * @param state the component state.
   * @return true if the component has been constructed.
   */
  public static boolean hasBeenConstructed( final byte state )
  {
    return hasBeenInitialized( state ) && COMPONENT_INITIALIZED != state;
  }

  /**
   * Return true if the component is active and can be interacted with.
   * This means that the component has been constructed and has not started to be disposed.
   *
   * @param state the component state.
   * @return true if the component is active.
   */
  public static boolean isActive( final byte state )
  {
    return COMPONENT_CONSTRUCTED == state || COMPONENT_READY == state;
  }

  /**
   * Return true if the component is disposing or disposed.
   *
   * @param state the component state.
   * @return true if the component is disposing or disposed.
   */
  public static boolean isDisposingOrDisposed( final byte state )
  {
    return state < 0;
  }

  /**
   * Describe specified state. This is usually used to provide error messages.
   *
   * @param state the component state.
   * @return a string description of the state.
   */
  @Nonnull
  public static String describe( final byte state )
  {
    switch ( state )
    {
      case COMPONENT_CREATED:
        return "created";
      case COMPONENT_INITIALIZED:
        return "initialized";
      case COMPONENT_CONSTRUCTED:
        return "constructed";
      case COMPONENT_READY:
        return "ready";
      case COMPONENT_DISPOSING:
        return "disposing";
      case COMPONENT_DISPOSED:
        return "disposed";
      default:
        throw new IllegalStateException( "Unexpected state passed into ComponentState.describe: " + state );
    }
  }

  private ComponentState()
  {
  }
}
