package com.example.observable;

import arez.EqualityComparator;
import arez.ObjectsDeepEqualsComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class ConflictingEqualityComparatorModel
{
  public static final class ObservableComparatorA
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      return null != oldValue;
    }
  }

  @Observable( equalityComparator = ObjectsDeepEqualsComparator.class )
  public abstract String getField();

  @Observable( equalityComparator = ObservableComparatorA.class )
  public abstract void setField( String field );
}
