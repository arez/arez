package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import javax.annotation.Nonnull;

@ArezComponent
abstract class Suppressed1PublicAccessObservableValueRefModel
{
  @Observable
  public abstract long getTime();

  public abstract void setTime( long time );

  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:PublicRefMethod" )
  @Nonnull
  @ObservableValueRef
  public abstract ObservableValue<Long> getTimeObservableValue();
}
