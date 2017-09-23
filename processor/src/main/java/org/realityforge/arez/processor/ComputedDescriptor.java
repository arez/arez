package org.realityforge.arez.processor;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;

/**
 * The class that represents the parsed state of @Computed methods on a @Container annotated class.
 */
final class ComputedDescriptor
{
  @Nonnull
  private final String _name;
  @Nullable
  private ExecutableElement _computed;
  @Nullable
  private ExecutableElement _onActivate;
  @Nullable
  private ExecutableElement _onDeactivate;
  @Nullable
  private ExecutableElement _onStale;
  @Nullable
  private ExecutableElement _onDispose;

  ComputedDescriptor( @Nonnull final String name )
  {
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  String getName()
  {
    return _name;
  }

  boolean hasComputed()
  {
    return null != _computed;
  }

  @Nonnull
  ExecutableElement getComputed()
  {
    return Objects.requireNonNull( _computed );
  }

  @Nullable
  ExecutableElement getOnActivate()
  {
    return _onActivate;
  }

  @Nullable
  ExecutableElement getOnDeactivate()
  {
    return _onDeactivate;
  }

  @Nullable
  ExecutableElement getOnStale()
  {
    return _onStale;
  }

  @Nullable
  ExecutableElement getOnDispose()
  {
    return _onDispose;
  }

  void setComputed( @Nonnull final ExecutableElement computed )
  {
    _computed = Objects.requireNonNull( computed );
  }

  void setOnActivate( @Nonnull final ExecutableElement onActivate )
  {
    _onActivate = Objects.requireNonNull( onActivate );
  }

  void setOnDeactivate( @Nonnull final ExecutableElement onDeactivate )
  {
    _onDeactivate = Objects.requireNonNull( onDeactivate );
  }

  void setOnStale( @Nonnull final ExecutableElement onStale )
  {
    _onStale = Objects.requireNonNull( onStale );
  }

  void setOnDispose( @Nonnull final ExecutableElement onDispose )
  {
    _onDispose = Objects.requireNonNull( onDispose );
  }

  @Nonnull
  ExecutableElement getDefiner()
  {
    if ( null != _computed )
    {
      return _computed;
    }
    else if ( null != _onActivate )
    {
      return _onActivate;
    }
    else if ( null != _onDeactivate )
    {
      return _onDeactivate;
    }
    else if ( null != _onDispose )
    {
      return _onDispose;
    }
    else
    {
      return Objects.requireNonNull( _onStale );
    }
  }
}
