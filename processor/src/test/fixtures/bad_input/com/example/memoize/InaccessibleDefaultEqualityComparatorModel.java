package com.example.memoize;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class InaccessibleDefaultEqualityComparatorModel
{
  @NullMarked
  private static final class HiddenComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final @Nullable Object oldValue, final @Nullable Object newValue )
    {
      return oldValue == newValue;
    }
  }

  @NullMarked
  @DefaultEqualityComparator( HiddenComparator.class )
  interface LabelView
  {
  }

  @Memoize
  @Nullable LabelView getValue()
  {
    return null;
  }
}
