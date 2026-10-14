package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ObservableNonnullMapModel
{
  @NonNull
  @Observable
  public Map<String, String> getMyValue()
  {
    return new HashMap<>();
  }

  public void setMyValue( final @Nullable Map<String, String> value )
  {
  }
}
