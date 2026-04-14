package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.annotations.Observable;
import arez.component.ComponentObservable;

@ArezComponent
abstract class ConcreteObservableAutoObserveModel
{
  @AutoObserve
  @Observable
  MyType getValue()
  {
    return null;
  }

  void setValue( final MyType value )
  {
  }

  static class MyType
    implements ComponentObservable
  {
    @Override
    public boolean observe()
    {
      return true;
    }
  }
}
