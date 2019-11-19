package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChange;

@ArezComponent
abstract class NonStandardMethodNameObserverRefModel
{
  @Observe( executor = Executor.EXTERNAL, name = "render" )
  public void $$$rende$$$r()
  {
  }

  @OnDepsChange( name = "render" )
  public void onRenderDepsChan$$$$$ge()
  {
  }

  @ObserverRef( name = "render" )
  abstract Observer observe$$$r();
}
