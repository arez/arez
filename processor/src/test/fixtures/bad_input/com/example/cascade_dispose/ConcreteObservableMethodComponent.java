package com.example.cascade_dispose;

import arez.Disposable;
import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class ConcreteObservableMethodComponent
{
  @CascadeDispose
  @Nullable Disposable getField()
  {
    return null;
  }

  @Observable
  void setField( @Nullable Disposable v )
  {
  }
}
