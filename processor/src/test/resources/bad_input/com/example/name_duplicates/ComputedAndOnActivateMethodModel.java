package com.example.name_duplicates;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.OnActivate;

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
