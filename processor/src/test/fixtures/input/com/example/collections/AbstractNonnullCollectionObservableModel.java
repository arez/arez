package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import java.util.Collection;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class AbstractNonnullCollectionObservableModel
{
  @NonNull
  @Observable
  public abstract Collection<String> getMyValue();

  public abstract void setMyValue( @Nullable Collection<String> value );
}
