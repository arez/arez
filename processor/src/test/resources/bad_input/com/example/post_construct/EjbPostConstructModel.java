package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Memoize;

@ArezComponent
public abstract class EjbPostConstructModel
{
  @Memoize
  public int getMyValue()
  {
    return 0;
  }

  @javax.annotation.PostConstruct
  void postConstruct()
  {
  }
}
