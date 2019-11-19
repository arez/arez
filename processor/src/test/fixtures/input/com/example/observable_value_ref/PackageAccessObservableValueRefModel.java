package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import javax.annotation.Nonnull;

@ArezComponent
abstract class PackageAccessObservableValueRefModel
{
  @Observable
  public abstract long getTime();

  public abstract void setTime( long time );

  @Nonnull
  @ObservableValueRef
  abstract ObservableValue<Long> getTimeObservableValue();
}
