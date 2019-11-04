package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Observable;

@ArezComponent
public abstract class ConcreteObservableMethodComponent
{
  @CascadeDispose
  Disposable getField()
  {
    return null;
  }

  @Observable
  void setField( Disposable v )
  {
  }
}
