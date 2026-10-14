package com.example.observe;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class NonStandardNameTrackedModel
{
  @Observe( executor = Executor.EXTERNAL, name = "render" )
  public void ren$$$der( final long $$time, float $$someOtherParameter )
  {
  }

  @OnDepsChange( name = "render" )
  void onRenderDepsCha$$$$$$nge()
  {
  }
}
