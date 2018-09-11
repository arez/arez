package com.example.observed.other;

import arez.annotations.Executor;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

public class BaseModelProtectedAccess
{
  @Observed( executor = Executor.APPLICATION )
  protected void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  protected final void onRenderDepsChanged()
  {
  }
}
