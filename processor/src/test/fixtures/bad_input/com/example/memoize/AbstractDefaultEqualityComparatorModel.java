package com.example.memoize;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class AbstractDefaultEqualityComparatorModel
{
  @NullMarked
  abstract static class AbstractComparator
    implements EqualityComparator
  {
  }

  @NullMarked
  @DefaultEqualityComparator( AbstractComparator.class )
  interface LabelView
  {
  }

  @Memoize
  @Nullable LabelView getValue()
  {
    return null;
  }
}
