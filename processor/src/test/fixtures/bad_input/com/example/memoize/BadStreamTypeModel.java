package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.stream.Stream;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
public abstract class BadStreamTypeModel
{
  @Memoize
  public @Nullable Stream<Integer> setField()
  {
    return null;
  }
}
