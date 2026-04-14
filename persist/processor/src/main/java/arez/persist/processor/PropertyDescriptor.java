package arez.persist.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;

final class PropertyDescriptor
{
  @Nonnull
  private final String _name;
  @Nonnull
  private final String _store;
  @Nonnull
  private final ExecutableElement _getter;
  @Nonnull
  private final ExecutableElement _setter;

  PropertyDescriptor( @Nonnull final String name,
                      @Nonnull final String store,
                      @Nonnull final ExecutableElement getter,
                      @Nonnull final ExecutableElement setter )
  {
    _name = Objects.requireNonNull( name );
    _store = Objects.requireNonNull( store );
    _getter = Objects.requireNonNull( getter );
    _setter = Objects.requireNonNull( setter );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  @Nonnull
  String getConstantName()
  {
    return "PROPERTY_" + getName();
  }

  @Nonnull
  String getStore()
  {
    return _store;
  }

  @Nonnull
  ExecutableElement getGetter()
  {
    return _getter;
  }

  @Nonnull
  ExecutableElement getSetter()
  {
    return _setter;
  }
}
