package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class MemoizeMapModel
{
  @Memoize
  public @Nullable Map<String, String> getMyValue()
  {
    return new HashMap<>();
  }
}
