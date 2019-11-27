package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

@ArezComponent
abstract class PublicAccessViaInterfaceOnDepsChangeModel
  implements OnDepsChangeInterface
{
  @Observe( executor = Executor.EXTERNAL )
  public void render()
  {
  }

  @Override
  @OnDepsChange
  public void onRenderDepsChange()
  {
  }
}
