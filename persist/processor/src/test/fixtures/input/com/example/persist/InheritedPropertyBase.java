package com.example.persist;

import arez.annotations.ArezComponentLike;
import arez.annotations.Observable;
import arez.persist.Persist;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponentLike
abstract class InheritedPropertyBase
{
  @Persist
  @Observable
  public abstract @Nullable String getValue();

  public abstract void setValue( @Nullable String value );
}
