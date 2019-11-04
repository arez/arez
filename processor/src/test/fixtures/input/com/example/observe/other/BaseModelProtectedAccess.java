package com.example.observe.other;

import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

public class BaseModelProtectedAccess
{
  @Observe( executor = Executor.EXTERNAL )
  protected void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChange
  protected final void onRenderDepsChange()
  {
  }
}
