package org.realityforge.arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;

/**
 * The class that represents the parsed state of @Computed methods on a @Container annotated class.
 */
final class ComputedDescriptor
{
  @Nonnull
  private final String _name;
  @Nonnull
  private final ExecutableElement _computed;

  ComputedDescriptor( @Nonnull final String name,
                      @Nonnull final ExecutableElement computed )
  {
    _name = Objects.requireNonNull( name );
    _computed = Objects.requireNonNull( computed );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  @Nonnull
  ExecutableElement getComputed()
  {
    return _computed;
  }
}
