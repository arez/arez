package com.example.package_access.other;

import arez.annotations.Observe;
import arez.annotations.OnDepsChange;

public abstract class BaseOnDepsChangeModel
{
  @Observe
  protected void render()
  {
  }

  @OnDepsChange
  void onRenderDepsChange()
  {
  }
}
