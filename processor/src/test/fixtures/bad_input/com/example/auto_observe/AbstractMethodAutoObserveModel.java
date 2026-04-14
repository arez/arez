package com.example.auto_observe;

import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;
import arez.component.ComponentObservable;

@ArezComponent
abstract class AbstractMethodAutoObserveModel
{
  @AutoObserve
  abstract MyType getValue();

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
