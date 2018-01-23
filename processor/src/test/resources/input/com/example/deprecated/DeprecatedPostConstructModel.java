package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import javax.annotation.PostConstruct;

@ArezComponent
public class DeprecatedPostConstructModel
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
