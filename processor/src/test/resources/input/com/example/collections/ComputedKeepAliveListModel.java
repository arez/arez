package com.example.collections;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import java.util.ArrayList;
import java.util.List;

@ArezComponent
public abstract class ComputedKeepAliveListModel
{
  @Computed( keepAlive = true )
  public List<String> getMyValue()
  {
    return new ArrayList<>();
  }
}
