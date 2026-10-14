package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class WriteOutsideTransactionDisabledObservableModel
{
  @Observable( writeOutsideTransaction = Feature.DISABLE )
  public abstract long getTime();

  public abstract void setTime( long value );
}
