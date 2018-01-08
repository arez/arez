package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChanged;
import arez.annotations.Track;

@ArezComponent
public class RefOnBothModel
{
  @Track
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }

  @ObserverRef
  Observer getRenderObserver()
  {
    throw new IllegalStateException();
  }

  @Autorun
  public void doStuff()
  {
  }

  @ObserverRef
  Observer getDoStuffObserver()
  {
    throw new IllegalStateException();
  }
}
