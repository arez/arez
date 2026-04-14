package com.example.memoize;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Memoize;

@ArezComponent
abstract class NoArgConstructorDefaultEqualityComparatorModel
{
  static final class NoDefaultConstructorComparator
    implements EqualityComparator
  {
    NoDefaultConstructorComparator( final String value )
    {
    }

    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      return oldValue == newValue;
    }
  }

  @DefaultEqualityComparator( NoDefaultConstructorComparator.class )
  interface LabelView
  {
  }

  @Memoize
  LabelView getValue()
  {
    return null;
  }
}
