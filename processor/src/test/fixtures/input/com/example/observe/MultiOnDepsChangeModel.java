package com.example.observe;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import javax.annotation.Nonnull;

@ArezComponent
abstract class MultiOnDepsChangeModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render( final long time, float someOtherParameter )
  {
  }

  void onRenderDepsChange()
  {
  }

  @OnDepsChange( name = "render" )
  void onRenderDepsChange2( @Nonnull final Observer observer )
  {
  }
}
