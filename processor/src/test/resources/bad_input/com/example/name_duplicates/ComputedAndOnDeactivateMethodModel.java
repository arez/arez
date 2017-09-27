package com.example.name_duplicates;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.OnDeactivate;

@ArezComponent
public class ComputedAndOnDeactivateMethodModel
{
  @Computed
  @OnDeactivate
  public long getField()
  {
    return 22;
  }
}
