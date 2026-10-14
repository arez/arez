package com.example.package_access.other;

import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import org.jspecify.annotations.NullMarked;

@NullMarked
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
