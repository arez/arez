package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.Collection;
import java.util.HashSet;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class MemoizeCollectionModel
{
  @Memoize
  public @Nullable Collection<String> getMyValue()
  {
    return new HashSet<>();
  }
}
