package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import javax.annotation.PostConstruct;

@ArezComponent
public class PostConstructModel
{
  @PostConstruct
  void postConstruct()
  {
  }

  @Computed
  public int someValue()
  {
    return 0;
  }
}
