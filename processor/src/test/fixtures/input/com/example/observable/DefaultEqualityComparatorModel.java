package com.example.observable;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.DefaultEqualityComparator;
import arez.annotations.Observable;
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

  private @Nullable LabelView _derived;
  private @Nullable LabelView _explicit;
  private @Nullable String _fallback;
  private @Nullable DerivedLabel _exactTypeOnly;

  @Nullable LabelView getDerived()
  {
    return _derived;
  }

  @Observable( setterAlwaysMutates = false )
  void setDerived( final @Nullable LabelView derived )
  {
    _derived = derived;
  }

  @Nullable LabelView getExplicit()
  {
    return _explicit;
  }

  @Observable( setterAlwaysMutates = false, equalityComparator = ExactComparator.class )
  void setExplicit( final @Nullable LabelView explicit )
  {
    _explicit = explicit;
  }

  @Nullable String getFallback()
  {
    return _fallback;
  }

  @Observable( setterAlwaysMutates = false )
  void setFallback( final @Nullable String fallback )
  {
    _fallback = fallback;
  }

  @Nullable DerivedLabel getExactTypeOnly()
  {
    return _exactTypeOnly;
  }

  @Observable( setterAlwaysMutates = false )
  void setExactTypeOnly( final @Nullable DerivedLabel exactTypeOnly )
  {
    _exactTypeOnly = exactTypeOnly;
  }
}
