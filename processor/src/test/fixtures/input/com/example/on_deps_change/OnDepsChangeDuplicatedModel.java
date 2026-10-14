package com.example.on_deps_change;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Observe;
import arez.annotations.OnDepsChange;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
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
  void onRenderDepsChange2( @NonNull final Observer observer )
  {
  }
}
