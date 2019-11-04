package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Date;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class AbstractNonPrimitiveNonnullObservablesModel
{
  @Observable
  @Nonnull
  public abstract Date getTime();

  public abstract void setTime( @Nonnull Date value );
}
