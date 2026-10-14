package com.example.persist;

import arez.annotations.Observable;
import arez.persist.Persist;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class GenericPropertyBase<T extends @Nullable Object>
{
  @Persist
  @Observable
  public abstract T getValue();

  public abstract void setValue(T value);
}
