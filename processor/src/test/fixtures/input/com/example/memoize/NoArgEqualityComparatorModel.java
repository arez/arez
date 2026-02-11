package com.example.memoize;

import arez.EqualityComparator;
import arez.ObjectsDeepEqualsComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
abstract class NoArgEqualityComparatorModel
{
  static final class MemoizeNoArgCustomComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      return null != oldValue;
    }
  }

  @Memoize
  int[] getDefaultValues()
  {
    return new int[]{ 1, 2, 3 };
  }

  @Memoize( equalityComparator = ObjectsDeepEqualsComparator.class )
  int[] getDeepValues()
  {
    return new int[]{ 1, 2, 3 };
  }

  @Memoize( equalityComparator = MemoizeNoArgCustomComparator.class )
  int[] getCustomValues()
  {
    return new int[]{ 1, 2, 3 };
  }
}
