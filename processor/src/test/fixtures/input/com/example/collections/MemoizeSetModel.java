package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.HashSet;
import java.util.Set;

@ArezComponent
public abstract class MemoizeSetModel
{
  @Memoize
  public Set<String> getMyValue()
  {
    return new HashSet<>();
  }
}
