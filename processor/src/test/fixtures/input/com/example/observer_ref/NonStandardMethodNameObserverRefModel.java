package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChange;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class NonStandardMethodNameObserverRefModel
{
  @Observe( executor = Executor.EXTERNAL, name = "render" )
  public void $$$rende$$$r()
  {
  }

  @OnDepsChange( name = "render" )
  void onRenderDepsChan$$$$$ge()
  {
  }

  @ObserverRef( name = "render" )
  abstract @Nullable Observer observe$$$r();
}
