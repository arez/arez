package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class AbstractListObservableModel
{
  @Observable
  public abstract @Nullable List<String> getMyValue();

  public abstract void setMyValue( @Nullable List<String> value );
}
