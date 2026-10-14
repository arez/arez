package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class AbstractNonnullMapObservableModel
{
  @NonNull
  @Observable
  public abstract Map<String, String> getMyValue();

  public abstract void setMyValue( @Nullable Map<String, String> value );
}
