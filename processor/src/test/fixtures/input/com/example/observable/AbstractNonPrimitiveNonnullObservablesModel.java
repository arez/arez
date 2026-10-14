package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Date;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class AbstractNonPrimitiveNonnullObservablesModel
{
  @Observable
  @NonNull
  public abstract Date getTime();

  public abstract void setTime( @NonNull Date value );
}
