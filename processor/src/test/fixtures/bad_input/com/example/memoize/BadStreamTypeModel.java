package com.example.memoize;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.stream.Stream;

@ArezComponent
public abstract class BadStreamTypeModel
{
  @Memoize
  public Stream<Integer> setField()
  {
    return null;
  }
}
