package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ReadOutsideTransactionEnabledObservableModel
{
  @Observable( readOutsideTransaction = Feature.ENABLE )
  public abstract long getTime();

  public abstract void setTime( long value );
}
