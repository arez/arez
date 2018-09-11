package com.example.on_deps_updated;

import arez.annotations.ArezComponent;
import arez.annotations.Observed;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class OnDepsChangedBadName3
{
  @Observed
  void render()
  {
  }

  @OnDepsChanged( name = "-ace-" )
  void onRenderDepsChanged()
  {
  }
}
