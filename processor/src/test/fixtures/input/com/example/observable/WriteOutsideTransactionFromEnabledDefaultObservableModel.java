package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent( defaultWriteOutsideTransaction = Feature.ENABLE )
abstract class WriteOutsideTransactionFromEnabledDefaultObservableModel
{
  @Observable
  public abstract long getTime();

  public abstract void setTime( long value );
}
