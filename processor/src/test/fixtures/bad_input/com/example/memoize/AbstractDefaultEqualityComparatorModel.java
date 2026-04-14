package com.example.memoize;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Memoize;

@ArezComponent
abstract class AbstractDefaultEqualityComparatorModel
{
  abstract static class AbstractComparator
    implements EqualityComparator
  {
  }

  @DefaultEqualityComparator( AbstractComparator.class )
  interface LabelView
  {
  }

  @Memoize
  LabelView getValue()
  {
    return null;
  }
}
