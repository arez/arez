package com.example.package_access.other;

import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

public abstract class BaseObserveModel
{
  @Observe
  void somePackageAccessAction()
  {
  }

  @Observe( executor = Executor.EXTERNAL )
  void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChange
  public void onRenderDepsChange()
  {
  }
}
