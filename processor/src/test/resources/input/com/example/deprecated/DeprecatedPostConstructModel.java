package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.PostConstruct;

@ArezComponent
public abstract class DeprecatedPostConstructModel
{
  @Deprecated
  @PostConstruct
  void postConstruct()
  {
  }

  @Computed
  public int someValue()
  {
    return 0;
  }
}
