package com.example.observer_ref;

import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.Executor;
import arez.annotations.Observed;
import arez.annotations.ObserverRef;
import arez.annotations.OnDepsChanged;

@ArezComponent
public abstract class CustomNameRefOnObservedModel2
{
  @Observed( executor = Executor.APPLICATION )
  public void render()
  {
  }

  @OnDepsChanged
  public void onRenderDepsChanged()
  {
  }

  @ObserverRef( name = "render" )
  abstract Observer observer();
}
