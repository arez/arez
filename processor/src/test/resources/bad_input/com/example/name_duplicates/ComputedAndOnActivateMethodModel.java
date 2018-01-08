package com.example.name_duplicates;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.OnActivate;

@ArezComponent
public class ComputedAndOnActivateMethodModel
{
  @Computed
  @OnActivate
  public long getField()
  {
    return 22;
  }
}
