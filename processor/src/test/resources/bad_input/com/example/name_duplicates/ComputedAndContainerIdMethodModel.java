package com.example.name_duplicates;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentId;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class ComputedAndContainerIdMethodModel
{
  @ComponentId
  @Computed
  public long getField()
  {
    return 22;
  }
}
