package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Map;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class AbstractMapObservableModel
{
  @Observable
  public abstract @Nullable Map<String, String> getMyValue();

  public abstract void setMyValue( @Nullable Map<String, String> value );
}
