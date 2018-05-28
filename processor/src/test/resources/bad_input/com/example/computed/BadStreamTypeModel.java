package com.example.computed;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import java.util.stream.Stream;

@ArezComponent
public abstract class BadStreamTypeModel
{
  @Computed
  public Stream<Integer> setField()
  {
    return null;
  }
}
