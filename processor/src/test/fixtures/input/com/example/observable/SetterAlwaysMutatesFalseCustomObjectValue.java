package com.example.observable;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
abstract class SetterAlwaysMutatesFalseCustomObjectValue
{
  static final class ObservableCustomComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      return null != oldValue && oldValue.equals( newValue );
    }
  }

  private String _field;

  String getField()
  {
    return _field;
  }

  @Observable( setterAlwaysMutates = false, equalityComparator = ObservableCustomComparator.class )
  void setField( final String field )
  {
    _field = field;
  }
}
