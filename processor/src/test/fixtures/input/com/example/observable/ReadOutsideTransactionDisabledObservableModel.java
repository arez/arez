package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent
abstract class ReadOutsideTransactionDisabledObservableModel
{
  @Observable( readOutsideTransaction = Feature.DISABLE )
  public abstract long getTime();

  public abstract void setTime( long value );
}
