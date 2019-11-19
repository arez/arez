package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;

@ArezComponent
abstract class MemoizeNonnullMapModel
{
  @Nonnull
  @Memoize
  public Map<String, String> getMyValue()
  {
    return new HashMap<>();
  }
}
