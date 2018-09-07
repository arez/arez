package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class SetterAlwaysMutatesFalsePrimitiveValue
{
  private int _field;

  public int getField()
  {
    return _field;
  }

  @Observable( setterAlwaysMutates = false )
  public void setField( int field )
  {
    _field = field;
  }
}
