package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.HashSet;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class MemoizeSetModel
{
  @Memoize
  public @Nullable Set<String> getMyValue()
  {
    return new HashSet<>();
  }
}
