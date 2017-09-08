package org.realityforge.arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;

/**
 * The class that represents the parsed state of @Action methods on a @Container annotated class.
 */
final class ActionDescriptor
{
  @Nonnull
  private final String _name;
  private final boolean _mutation;
  @Nonnull
  private final ExecutableElement _action;

  ActionDescriptor( @Nonnull final String name,
                    final boolean mutation,
                    @Nonnull final ExecutableElement action )
  {
    _name = Objects.requireNonNull( name );
    _mutation = mutation;
    _action = Objects.requireNonNull( action );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  boolean isMutation()
  {
    return _mutation;
  }

  @Nonnull
  ExecutableElement getAction()
  {
    return _action;
  }
}
