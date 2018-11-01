package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.Collection;
import java.util.HashSet;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class MemoizeNonnullCollectionModel
{
  @Nonnull
  @Memoize
  public Collection<String> getMyValue()
  {
    return new HashSet<>();
  }
}
