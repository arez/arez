package org.realityforge.arez.api2;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ArezElement
{
  @Nullable
  private final String _name;

  ArezElement( @Nullable final String name )
  {
    _name = ArezConfig.enableNames() ? Objects.requireNonNull( name ) : null;
    Guards.invariant( () -> ArezConfig.enableNames() || null == name,
                      () -> String.format( "ArezElement passed a name '%s' but ArezConfig.enableNames() is false", name ) );
  }

  @Nonnull
  public final String getName()
  {
    Guards.invariant( ArezConfig::enableNames,
                      () -> "ArezElement.getName() invoked when ArezConfig.enableNames() is false" );
    assert null != _name;
    return _name;
  }

  @Nonnull
  @Override
  public final String toString()
  {
    if ( ArezConfig.enableNames() )
    {
      return getName();
    }
    else
    {
      return super.toString();
    }
  }
}
