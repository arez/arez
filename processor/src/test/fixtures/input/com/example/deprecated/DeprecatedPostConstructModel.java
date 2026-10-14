package com.example.deprecated;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostConstruct;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class DeprecatedPostConstructModel
{
  @Deprecated
  @PostConstruct
  void postConstruct()
  {
  }

  @Memoize
  public int someValue()
  {
    return 0;
  }
}
