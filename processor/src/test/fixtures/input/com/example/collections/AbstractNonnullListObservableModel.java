package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class AbstractNonnullListObservableModel
{
  @NonNull
  @Observable
  public abstract List<String> getMyValue();

  public abstract void setMyValue( @Nullable List<String> value );
}
