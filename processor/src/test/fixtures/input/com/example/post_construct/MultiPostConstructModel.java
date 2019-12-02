package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.PostConstruct;

@ArezComponent( allowEmpty = true )
public abstract class MultiPostConstructModel
{
  @PostConstruct
  void postConstruct1()
  {
  }

  @PostConstruct
  void postConstruct2()
  {
  }
}
