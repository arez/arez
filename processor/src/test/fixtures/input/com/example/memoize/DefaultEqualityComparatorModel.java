package com.example.memoize;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Memoize;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class DefaultEqualityComparatorModel
{
  @NullMarked
  @DefaultEqualityComparator( IgnoreCaseComparator.class )
  interface LabelView
  {
  }

  @NullMarked
  @DefaultEqualityComparator( IgnoreCaseComparator.class )
  static final class DerivedLabel
  {
    @Override
    public @Nullable String toString()
    {
      return "derived";
    }
  }

  @NullMarked
  static final class Label
    implements LabelView
  {
    private final @Nullable String _value;

    Label( final @Nullable String value )
    {
      _value = value;
    }

    @Override
    public @Nullable String toString()
    {
      return _value;
    }
  }

  @NullMarked
  static final class IgnoreCaseComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final @Nullable Object oldValue, final @Nullable Object newValue )
    {
      return null == oldValue || null == newValue ?
             oldValue == newValue :
             oldValue.toString().equalsIgnoreCase( newValue.toString() );
    }
  }

  @NullMarked
  static final class ExactComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final @Nullable Object oldValue, final @Nullable Object newValue )
    {
      return null == oldValue || null == newValue ?
             oldValue == newValue :
             oldValue.toString().equals( newValue.toString() );
    }
  }

  @Memoize
  @Nullable LabelView defaultLabel()
  {
    return new Label( "Alpha" );
  }

  @Memoize
  @Nullable LabelView labelForSeed( final int seed )
  {
    return new Label( "Seed" + seed );
  }

  @Memoize( equalityComparator = ExactComparator.class )
  @Nullable LabelView explicitLabel()
  {
    return new Label( "Explicit" );
  }

  @Memoize
  @Nullable String fallbackLabel()
  {
    return "Fallback";
  }

  @Memoize
  @Nullable Object exactTypeOnlyLabel()
  {
    return new DerivedLabel();
  }
}
