package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.Collection;
import java.util.HashSet;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class MemoizeNonnullCollectionModel
{
  @NonNull
  @Memoize
  public Collection<String> getMyValue()
  {
    return new HashSet<>();
  }
}
