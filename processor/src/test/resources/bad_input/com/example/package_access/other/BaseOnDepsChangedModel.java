package com.example.package_access.other;

import arez.annotations.Observe;
import arez.annotations.OnDepsChanged;

public abstract class BaseOnDepsChangedModel
{
  @Observe
  protected void render()
  {
  }

  @OnDepsChanged
  void onRenderDepsChanged()
  {
  }
}
