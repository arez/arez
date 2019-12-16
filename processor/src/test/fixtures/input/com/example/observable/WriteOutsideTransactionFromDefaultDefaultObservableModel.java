package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent
abstract class WriteOutsideTransactionFromDefaultDefaultObservableModel
{
  @Observable
  public abstract long getTime();

  public abstract void setTime( long value );
}
