package com.example.post_construct;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Memoize;
import arez.annotations.PostConstruct;

@ArezComponent
abstract class ActionPostConstructModel
{
  @Action
  @PostConstruct
  void postConstruct()
  {
  }

  @Memoize
  public int someValue()
  {
    return 0;
  }
}
