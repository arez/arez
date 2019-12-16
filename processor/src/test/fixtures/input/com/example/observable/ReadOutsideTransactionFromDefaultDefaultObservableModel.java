package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
abstract class ReadOutsideTransactionFromDefaultDefaultObservableModel
{
  @Observable
  public abstract long getTime();

  public abstract void setTime( long value );
}
