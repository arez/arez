package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class AbstractSetObservableModel
{
  @Observable
  public abstract @Nullable Set<String> getMyValue();

  public abstract void setMyValue( @Nullable Set<String> value );
}
