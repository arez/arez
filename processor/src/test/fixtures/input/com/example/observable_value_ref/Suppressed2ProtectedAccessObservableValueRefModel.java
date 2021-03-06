package com.example.observable_value_ref;

import arez.ObservableValue;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableValueRef;
import arez.annotations.SuppressArezWarnings;
import javax.annotation.Nonnull;

@ArezComponent
abstract class Suppressed2ProtectedAccessObservableValueRefModel
{
  @Observable
  public abstract long getTime();

  public abstract void setTime( long time );

  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedMethod" )
  @Nonnull
  @ObservableValueRef
  protected abstract ObservableValue<Long> getTimeObservableValue();
}
