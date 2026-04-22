package com.example.observe;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.OnDepsChange;
import javax.annotation.Nonnull;

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
  void onRenderDepsChange2( @Nonnull final Observer observer )
  {
  }
}
