package com.example.observable;

import arez.ObjectsDeepEqualsComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
abstract class SetterAlwaysMutatesFalseDeepObjectValue
{
  private int[] _field;

  int[] getField()
  {
    return _field;
  }

  @Observable( setterAlwaysMutates = false, equalityComparator = ObjectsDeepEqualsComparator.class )
  void setField( final int[] field )
  {
    _field = field;
  }
}
