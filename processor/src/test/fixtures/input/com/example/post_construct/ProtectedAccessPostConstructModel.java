package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostConstruct;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class ProtectedAccessPostConstructModel
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
