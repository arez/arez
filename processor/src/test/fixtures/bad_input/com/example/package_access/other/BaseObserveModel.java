package com.example.package_access.other;

import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import org.jspecify.annotations.NullMarked;

@NullMarked
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
  void onRenderDepsChange()
  {
  }
}
