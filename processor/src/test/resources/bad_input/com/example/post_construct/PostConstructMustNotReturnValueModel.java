package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.PostConstruct;

@ArezComponent
public abstract class PostConstructMustNotReturnValueModel
{
  @PostConstruct
  int postConstruct()
  {
    return 0;
  }

  @Computed
  public int someValue()
  {
    return 0;
  }
}
