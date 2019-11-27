package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;

@ArezComponent
abstract class DeriveOnDepsChangeModel
{
  @Observe( executor = Executor.EXTERNAL )
  public void render( final long time, float someOtherParameter )
  {
  }

  void onRenderDepsChange()
  {
  }
}
