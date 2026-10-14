package com.example.memoize;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class NoArgConstructorDefaultEqualityComparatorModel
{
  @NullMarked
  static final class NoDefaultConstructorComparator
    implements EqualityComparator
  {
    NoDefaultConstructorComparator( final @Nullable String value )
    {
    }

    @Override
    public boolean areEqual( final @Nullable Object oldValue, final @Nullable Object newValue )
    {
      return oldValue == newValue;
    }
  }

  @NullMarked
  @DefaultEqualityComparator( NoDefaultConstructorComparator.class )
  interface LabelView
  {
  }

  @Memoize
  @Nullable LabelView getValue()
  {
    return null;
  }
}
