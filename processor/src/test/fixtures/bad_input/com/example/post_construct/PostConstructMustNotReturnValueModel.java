package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostConstruct;

@ArezComponent
public abstract class PostConstructMustNotReturnValueModel
{
  @PostConstruct
  int postConstruct()
  {
    return 0;
  }

  @Memoize
  public int someValue()
  {
    return 0;
  }
}
