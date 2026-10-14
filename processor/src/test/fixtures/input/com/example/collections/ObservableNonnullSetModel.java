package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.HashSet;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ObservableNonnullSetModel
{
  @NonNull
  @Observable
  public Set<String> getMyValue()
  {
    return new HashSet<>();
  }

  public void setMyValue( final @Nullable Set<String> value )
  {
  }
}
