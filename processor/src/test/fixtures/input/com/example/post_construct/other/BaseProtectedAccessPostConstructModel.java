package com.example.post_construct.other;

import arez.annotations.Memoize;
import arez.annotations.PostConstruct;

public abstract class BaseProtectedAccessPostConstructModel
{
  @PostConstruct
  protected void postConstruct()
  {
  }

  @Memoize
  public int someValue()
  {
    return 0;
  }
}
