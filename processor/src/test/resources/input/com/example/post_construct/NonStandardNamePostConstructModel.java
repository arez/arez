package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostConstruct;

@ArezComponent
public abstract class NonStandardNamePostConstructModel
{
  @PostConstruct
  void postConst$$$ruct()
  {
  }

  @Memoize
  public int someValue()
  {
    return 0;
  }
}
