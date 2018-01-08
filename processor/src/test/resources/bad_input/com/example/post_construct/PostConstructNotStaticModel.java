package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import javax.annotation.PostConstruct;

@ArezComponent
public class PostConstructNotStaticModel
{
  @PostConstruct
  static void postConstruct()
  {
  }

  @Computed
  public int someValue()
  {
    return 0;
  }
}
