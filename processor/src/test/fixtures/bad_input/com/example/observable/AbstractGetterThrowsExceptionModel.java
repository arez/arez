package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class AbstractGetterThrowsExceptionModel
{
  @SuppressWarnings( { "RedundantThrows", "RedundantSuppression" } )
  @Observable
  public abstract long getField()
    throws Exception;

  public abstract void setField( long v );
}
