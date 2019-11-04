package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class MemoizeNonnullSetModel
{
  @Nonnull
  @Memoize
  public Set<String> getMyValue()
  {
    return new HashSet<>();
  }
}
