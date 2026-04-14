package com.example.memoize;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Memoize;

@ArezComponent
abstract class InaccessibleDefaultEqualityComparatorModel
{
  private static final class HiddenComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      return oldValue == newValue;
    }
  }

  @DefaultEqualityComparator( HiddenComparator.class )
  interface LabelView
  {
  }

  @Memoize
  LabelView getValue()
  {
    return null;
  }
}
