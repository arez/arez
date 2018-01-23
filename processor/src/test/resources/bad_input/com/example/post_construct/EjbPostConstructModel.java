package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;

@ArezComponent
public class EjbPostConstructModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @javax.annotation.PostConstruct
  void postConstruct()
  {
  }
}
