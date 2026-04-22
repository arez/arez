package com.example.on_deps_change;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class OnDepsChangeDuplicatedModel
{
  @Observe
  void render()
  {
  }

  @OnDepsChange
  void onRenderDepsChange()
  {
  }

  @OnDepsChange( name = "render" )
  void onRenderDepsChange2( @Nonnull final Observer observer )
  {
  }
}
