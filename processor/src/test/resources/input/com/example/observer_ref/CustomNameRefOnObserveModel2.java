package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observe;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChange;

@ArezComponent
public abstract class CustomNameRefOnObserveModel2
{
  @Observe( executor = Executor.APPLICATION )
  public void render()
  {
  }

  @OnDepsChange
  public void onRenderDepsChange()
  {
  }

  @ObserverRef( name = "render" )
  abstract Observer observer();
}
