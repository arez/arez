package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class WriteOutsideTransactionObservablesModel
{
  @Observable( writeOutsideTransaction = true )
  public abstract long getTime();

  public abstract void setTime( long value );
}
