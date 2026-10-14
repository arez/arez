package com.example.memoize;

import arez.EqualityComparator;
import arez.ObjectsDeepEqualsComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class NoArgEqualityComparatorModel
{
  @NullMarked
  static final class MemoizeNoArgCustomComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final @Nullable Object oldValue, final @Nullable Object newValue )
    {
      return null != oldValue;
    }
  }

  @Memoize
  int@Nullable [] getDefaultValues()
  {
    return new int[]{ 1, 2, 3 };
  }

  @Memoize( equalityComparator = ObjectsDeepEqualsComparator.class )
  int@Nullable [] getDeepValues()
  {
    return new int[]{ 1, 2, 3 };
  }

  @Memoize( equalityComparator = MemoizeNoArgCustomComparator.class )
  int@Nullable [] getCustomValues()
  {
    return new int[]{ 1, 2, 3 };
  }
}
