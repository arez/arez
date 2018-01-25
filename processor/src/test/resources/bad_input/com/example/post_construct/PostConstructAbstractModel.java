package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.PostConstruct;

@ArezComponent
public abstract class PostConstructAbstractModel
{
  @PostConstruct
  abstract void postConstruct();

  @Computed
  public int someValue()
  {
    return 0;
  }
}
