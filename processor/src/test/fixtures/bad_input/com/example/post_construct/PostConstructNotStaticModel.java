package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostConstruct;
import org.jspecify.annotations.NullMarked;

@NullMarked
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
