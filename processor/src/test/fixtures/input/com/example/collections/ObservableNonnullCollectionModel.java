package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.ArrayList;
import java.util.Collection;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ObservableNonnullCollectionModel
{
  @NonNull
  @Observable
  public Collection<String> getMyValue()
  {
    return new ArrayList<>();
  }

  public void setMyValue( final @Nullable Collection<String> value )
  {
  }
}
