package com.example.memoize;

import arez.EqualityComparator;
import arez.ObjectsDeepEqualsComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ParameterizedEqualityComparatorModel
{
  @NullMarked
  static final class MemoizeParameterizedCustomComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final @Nullable Object oldValue, final @Nullable Object newValue )
    {
      return null != oldValue;
    }
  }

  @Memoize
  int@Nullable [] defaultValues( final int seed )
  {
    return new int[]{ seed, seed + 1, seed + 2 };
  }

  @Memoize( equalityComparator = ObjectsDeepEqualsComparator.class )
  int@Nullable [] deepValues( final int seed )
  {
    return new int[]{ seed, seed + 1, seed + 2 };
  }

  @Memoize( equalityComparator = MemoizeParameterizedCustomComparator.class )
  int@Nullable [] customValues( final int seed )
  {
    return new int[]{ seed, seed + 1, seed + 2 };
  }
}
