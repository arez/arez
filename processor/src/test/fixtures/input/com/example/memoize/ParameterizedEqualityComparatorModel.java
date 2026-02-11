package com.example.memoize;

import arez.EqualityComparator;
import arez.ObjectsDeepEqualsComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
abstract class ParameterizedEqualityComparatorModel
{
  static final class MemoizeParameterizedCustomComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      return null != oldValue;
    }
  }

  @Memoize
  int[] defaultValues( final int seed )
  {
    return new int[]{ seed, seed + 1, seed + 2 };
  }

  @Memoize( equalityComparator = ObjectsDeepEqualsComparator.class )
  int[] deepValues( final int seed )
  {
    return new int[]{ seed, seed + 1, seed + 2 };
  }

  @Memoize( equalityComparator = MemoizeParameterizedCustomComparator.class )
  int[] customValues( final int seed )
  {
    return new int[]{ seed, seed + 1, seed + 2 };
  }
}
