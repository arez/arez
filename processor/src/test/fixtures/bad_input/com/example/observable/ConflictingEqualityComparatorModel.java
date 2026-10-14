package com.example.observable;

import arez.EqualityComparator;
import arez.ObjectsDeepEqualsComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class ConflictingEqualityComparatorModel
{
  @NullMarked
  public static final class ObservableComparatorA
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final @Nullable Object oldValue, final @Nullable Object newValue )
    {
      return null != oldValue;
    }
  }

  @Observable( equalityComparator = ObjectsDeepEqualsComparator.class )
  public abstract @Nullable String getField();

  @Observable( equalityComparator = ObservableComparatorA.class )
  public abstract void setField( @Nullable String field );
}
