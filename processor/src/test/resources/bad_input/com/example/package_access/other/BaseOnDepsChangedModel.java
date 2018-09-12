package com.example.package_access.other;

import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

public abstract class BaseOnDepsChangedModel
{
  @Observed
  protected void render()
  {
  }

  @OnDepsChanged
  void onRenderDepsChanged()
  {
  }
}
