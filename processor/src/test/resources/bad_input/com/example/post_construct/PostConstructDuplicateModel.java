package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import javax.annotation.PostConstruct;

@ArezComponent
public class PostConstructDuplicateModel
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
