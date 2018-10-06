package com.example.observed.other;

import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;

public class BaseModelProtectedAccess
{
  @Observe( executor = Executor.APPLICATION )
  protected void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  protected final void onRenderDepsChanged()
  {
  }
}
