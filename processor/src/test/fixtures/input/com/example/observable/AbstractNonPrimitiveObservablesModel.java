package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Date;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class AbstractNonPrimitiveObservablesModel
{
  @Observable
  public abstract @Nullable Date getTime();

  public abstract void setTime( @Nullable Date value );
}
