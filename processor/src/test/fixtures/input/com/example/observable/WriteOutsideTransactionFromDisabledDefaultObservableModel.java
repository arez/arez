package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent( defaultWriteOutsideTransaction = Feature.DISABLE )
abstract class WriteOutsideTransactionFromDisabledDefaultObservableModel
{
  @Observable
  public abstract long getTime();

  public abstract void setTime( long value );
}
