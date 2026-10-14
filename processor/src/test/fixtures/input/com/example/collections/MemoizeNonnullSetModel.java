package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.HashSet;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class MemoizeNonnullSetModel
{
  @NonNull
  @Memoize
  public Set<String> getMyValue()
  {
    return new HashSet<>();
  }
}
