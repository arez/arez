package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostConstruct;

@ArezComponent
public abstract class PostConstructNotStaticModel
{
  @PostConstruct
  static void postConstruct()
  {
  }

  @Memoize
  public int someValue()
  {
    return 0;
  }
}
