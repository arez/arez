package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ReadOutsideTransactionDisabledObservableModel
{
  @Observable( readOutsideTransaction = Feature.DISABLE )
  public abstract long getTime();

  public abstract void setTime( long value );
}
