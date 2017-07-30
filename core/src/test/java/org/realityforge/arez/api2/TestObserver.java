package org.realityforge.arez.api2;

import java.util.Objects;
import javax.annotation.Nonnull;
import org.realityforge.guiceyloops.shared.ValueUtil;

class TestObserver
  implements Observer
{
  private final String _name;
  private ObserverState _state = ObserverState.NOT_TRACKING;

  TestObserver()
  {
    this( ValueUtil.randomString() );
  }

  TestObserver( @Nonnull final String name )
  {
    _name = Objects.requireNonNull( name );
  }

  @Nonnull
  @Override
  public String getName()
  {
    return _name;
  }

  @Nonnull
  @Override
  public ObserverState getState()
  {
    return _state;
  }

  @Override
  public void setState( @Nonnull final ObserverState state )
  {
    _state = state;
  }
}
