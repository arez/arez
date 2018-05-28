package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

@ArezComponent
public abstract class ComputedNonnullListModel
{
  @Nonnull
  @Computed
  public List<String> getMyValue()
  {
    return new ArrayList<>();
  }
}
