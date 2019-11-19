package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
abstract class SetterAlwaysMutatesFalseObjectValue
{
  private String _field;

  public String getField()
  {
    return _field;
  }

  @Observable( setterAlwaysMutates = false )
  public void setField( String field )
  {
    _field = field;
  }
}
