package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent( defaultReadOutsideTransaction = Feature.DISABLE )
abstract class ReadOutsideTransactionFromDisabledDefaultObservableModel
{
  @Observable
  public abstract long getTime();

  public abstract void setTime( long value );
}
