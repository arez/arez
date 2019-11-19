package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChange;

@ArezComponent
abstract class ExternalObserveObserverRefModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChange
  public void onRenderDepsChange()
  {
  }

  @ObserverRef
  abstract Observer getRenderObserver();
}
