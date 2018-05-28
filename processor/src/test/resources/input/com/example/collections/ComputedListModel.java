package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import java.util.ArrayList;
import java.util.List;

@ArezComponent
public abstract class ComputedListModel
{
  @Computed
  public List<String> getMyValue()
  {
    return new ArrayList<>();
  }
}
