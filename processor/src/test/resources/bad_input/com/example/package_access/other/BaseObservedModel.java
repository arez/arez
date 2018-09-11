package com.example.package_access.other;

import arez.annotations.Executor;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

public abstract class BaseObservedModel
{
  @Observed
  void somePackageAccessAction()
  {
  }

  @Observed( executor = Executor.APPLICATION )
  void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
