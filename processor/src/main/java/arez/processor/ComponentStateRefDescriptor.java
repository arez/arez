package arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;

/**
 * Declaration of a method that used to access component state.
 */
final class ComponentStateRefDescriptor
{
  enum State
  {
    CONSTRUCTED,
    COMPLETE,
    READY,
    DISPOSING
  }

  @Nonnull
  private final ExecutableElement _method;
  @Nonnull
  private final State _state;

  ComponentStateRefDescriptor( @Nonnull final ExecutableElement method, @Nonnull final State state )
  {
    _method = Objects.requireNonNull( method );
    _state = Objects.requireNonNull( state );
  }

  @Nonnull
  ExecutableElement getMethod()
  {
    return _method;
  }

  @Nonnull
  State getState()
  {
    return _state;
  }
}
