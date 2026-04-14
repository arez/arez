package com.example.memoize;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Memoize;

@ArezComponent
abstract class DefaultEqualityComparatorModel
{
  @DefaultEqualityComparator( IgnoreCaseComparator.class )
  interface LabelView
  {
  }

  @DefaultEqualityComparator( IgnoreCaseComparator.class )
  static final class DerivedLabel
  {
    @Override
    public String toString()
    {
      return "derived";
    }
  }

  static final class Label
    implements LabelView
  {
    private final String _value;

    Label( final String value )
    {
      _value = value;
    }

    @Override
    public String toString()
    {
      return _value;
    }
  }

  static final class IgnoreCaseComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      return null == oldValue || null == newValue ?
             oldValue == newValue :
             oldValue.toString().equalsIgnoreCase( newValue.toString() );
    }
  }

  static final class ExactComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final Object oldValue, final Object newValue )
    {
      return null == oldValue || null == newValue ?
             oldValue == newValue :
             oldValue.toString().equals( newValue.toString() );
    }
  }

  @Memoize
  LabelView defaultLabel()
  {
    return new Label( "Alpha" );
  }

  @Memoize
  LabelView labelForSeed( final int seed )
  {
    return new Label( "Seed" + seed );
  }

  @Memoize( equalityComparator = ExactComparator.class )
  LabelView explicitLabel()
  {
    return new Label( "Explicit" );
  }

  @Memoize
  String fallbackLabel()
  {
    return "Fallback";
  }

  @Memoize
  Object exactTypeOnlyLabel()
  {
    return new DerivedLabel();
  }
}
