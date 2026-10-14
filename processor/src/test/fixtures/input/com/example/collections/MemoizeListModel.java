package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class MemoizeListModel
{
  @Memoize
  public @Nullable List<String> getMyValue()
  {
    return new ArrayList<>();
  }
}
