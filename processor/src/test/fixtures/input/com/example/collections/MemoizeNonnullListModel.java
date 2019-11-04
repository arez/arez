package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class MemoizeNonnullListModel
{
  @Nonnull
  @Memoize
  public List<String> getMyValue()
  {
    return new ArrayList<>();
  }
}
