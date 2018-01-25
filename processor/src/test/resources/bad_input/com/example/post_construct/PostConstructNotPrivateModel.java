package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.PostConstruct;

@ArezComponent
public abstract class PostConstructNotPrivateModel
{
  @PostConstruct
  private void postConstruct()
  {
  }

  @Computed
  public int someValue()
  {
    return 0;
  }
}
