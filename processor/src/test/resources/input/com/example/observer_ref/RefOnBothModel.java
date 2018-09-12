package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class RefOnBothModel
{
  @Observed( executor = Executor.APPLICATION )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }

  @ObserverRef
  abstract Observer getRenderObserver();

  @Observed
  protected void doStuff()
  {
  }

  @ObserverRef
  abstract Observer getDoStuffObserver();
}
