package com.example.observe;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChange;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class MultiInternalOnDepsChangeModel
{
  void render()
  {
  }

  void onRenderDepsChange()
  {
  }

  @OnDepsChange( name = "render" )
  void onRenderDepsChange2( @NonNull final Observer observer )
  {
  }
}
