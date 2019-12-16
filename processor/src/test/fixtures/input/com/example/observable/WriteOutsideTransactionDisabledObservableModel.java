package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent
abstract class WriteOutsideTransactionDisabledObservableModel
{
  @Observable( writeOutsideTransaction = Feature.DISABLE )
  public abstract long getTime();

  public abstract void setTime( long value );
}
