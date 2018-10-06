package com.example.package_access.other;

import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;

public abstract class BaseObserveModel
{
  @Observe
  void somePackageAccessAction()
  {
  }

  @Observe( executor = Executor.APPLICATION )
  void render( final long time, float someOtherParameter )
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }
}
