package org.realityforge.arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;

/**
 * The class that represents the parsed state of @Autorun methods on a @Container annotated class.
 */
final class AutorunDescriptor
{
  @Nonnull
  private final String _name;
  private final boolean _mutation;
  @Nonnull
  private final ExecutableElement _autorun;

  AutorunDescriptor( @Nonnull final String name,
                     final boolean mutation,
                     @Nonnull final ExecutableElement autorun )
  {
    _name = Objects.requireNonNull( name );
    _mutation = mutation;
    _autorun = Objects.requireNonNull( autorun );
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
  ExecutableElement getAutorun()
  {
    return _autorun;
  }
}
