package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class AbstractCollectionObservableModel
{
  @Observable
  public abstract @Nullable Collection<String> getMyValue();

  public abstract void setMyValue( @Nullable Collection<String> value );
}
