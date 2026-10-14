package com.example.observable;

import arez.ObjectsDeepEqualsComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class SetterAlwaysMutatesFalseDeepObjectValue
{
  private int@Nullable [] _field;

  int@Nullable [] getField()
  {
    return _field;
  }

  @Observable( setterAlwaysMutates = false, equalityComparator = ObjectsDeepEqualsComparator.class )
  void setField( final int@Nullable [] field )
  {
    _field = field;
  }
}
