package com.example.observable;

import arez.EqualityComparator;
import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class SetterAlwaysMutatesFalseCustomObjectValue
{
  @NullMarked
  static final class ObservableCustomComparator
    implements EqualityComparator
  {
    @Override
    public boolean areEqual( final @Nullable Object oldValue, final @Nullable Object newValue )
    {
      return null != oldValue && oldValue.equals( newValue );
    }
  }

  private @Nullable String _field;

  @Nullable String getField()
  {
    return _field;
  }

  @Observable( setterAlwaysMutates = false, equalityComparator = ObservableCustomComparator.class )
  void setField( final @Nullable String field )
  {
    _field = field;
  }
}
