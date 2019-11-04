package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.Collection;
import java.util.HashSet;

@ArezComponent
public abstract class MemoizeCollectionModel
{
  @Memoize
  public Collection<String> getMyValue()
  {
    return new HashSet<>();
  }
}
