package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class SetterAlwaysMutatesFalseOnAbstractMethods
{
  public abstract long getField();

  @Observable( setterAlwaysMutates = false )
  public abstract void setField( long field );
}
