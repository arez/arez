package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ObservableCollectionModel
{
  @Observable
  public @Nullable Collection<String> getMyValue()
  {
    return null;
  }

  public void setMyValue( final @Nullable Collection<String> value )
  {
  }
}
