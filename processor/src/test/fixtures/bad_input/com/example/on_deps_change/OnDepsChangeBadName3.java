package com.example.on_deps_change;

import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class OnDepsChangeBadName3
{
  @Observe
  void render()
  {
  }

  @OnDepsChange( name = "-ace-" )
  void onRenderDepsChange()
  {
  }
}
