package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import arez.annotations.PostConstruct;

@ArezComponent
public abstract class PostConstructDuplicateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @PostConstruct
  void postConstruct1()
  {
  }

  @PostConstruct
  void postConstruct2()
  {
  }
}
