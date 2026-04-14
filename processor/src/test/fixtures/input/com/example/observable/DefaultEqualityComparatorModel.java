package com.example.observable;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Observable;

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

  private LabelView _derived;
  private LabelView _explicit;
  private String _fallback;
  private DerivedLabel _exactTypeOnly;

  LabelView getDerived()
  {
    return _derived;
  }

  @Observable( setterAlwaysMutates = false )
  void setDerived( final LabelView derived )
  {
    _derived = derived;
  }

  LabelView getExplicit()
  {
    return _explicit;
  }

  @Observable( setterAlwaysMutates = false, equalityComparator = ExactComparator.class )
  void setExplicit( final LabelView explicit )
  {
    _explicit = explicit;
  }

  String getFallback()
  {
    return _fallback;
  }

  @Observable( setterAlwaysMutates = false )
  void setFallback( final String fallback )
  {
    _fallback = fallback;
  }

  DerivedLabel getExactTypeOnly()
  {
    return _exactTypeOnly;
  }

  @Observable( setterAlwaysMutates = false )
  void setExactTypeOnly( final DerivedLabel exactTypeOnly )
  {
    _exactTypeOnly = exactTypeOnly;
  }
}
