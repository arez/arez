package org.realityforge.arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;

/**
 * The class that represents the parsed state of Observable properties on a @Container annotated class.
 */
final class ObservableDescriptor
{
  @Nonnull
  private final ContainerDescriptor _containerDescriptor;
  @Nonnull
  private final String _name;
  @Nullable
  private ExecutableElement _getter;
  @Nullable
  private ExecutableElement _setter;

  ObservableDescriptor( @Nonnull final ContainerDescriptor containerDescriptor, @Nonnull final String name )
  {
    _containerDescriptor = Objects.requireNonNull( containerDescriptor );
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  boolean hasGetter()
  {
    return null != _getter;
  }

  @Nonnull
  ExecutableElement getGetter()
    throws ArezProcessorException
  {
    if ( null == _getter )
    {
      throw new ArezProcessorException( String.format( "ObservableDescriptor.getGetter() invoked for observable " +
                                                       "named '%s' on container named '%s' before getter has " +
                                                       "been set", getName(), _containerDescriptor.getName() ),
                                        Objects.requireNonNull( _setter ) );
    }
    return _getter;
  }

  void setGetter( @Nonnull final ExecutableElement getter )
  {
    _getter = Objects.requireNonNull( getter );
  }

  boolean hasSetter()
  {
    return null != _setter;
  }

  @Nonnull
  ExecutableElement getSetter()
    throws ArezProcessorException
  {
    if ( null == _setter )
    {
      throw new ArezProcessorException( String.format( "ObservableDescriptor.getSetter() invoked for observable " +
                                                       "named '%s' on container named '%s' before setter has " +
                                                       "been set", getName(), _containerDescriptor.getName() ),
                                        Objects.requireNonNull( _setter ) );
    }
    return _setter;
  }

  void setSetter( @Nonnull final ExecutableElement setter )
  {
    _setter = Objects.requireNonNull( setter );
  }
}
