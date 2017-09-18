package com.example.computed;

import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class TypeParametersModel
{
  @Computed
  public <T extends Integer> T getTime()
  {
    return null;
  }
}
