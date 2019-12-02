package com.example.post_construct.other;

import arez.annotations.PostConstruct;

public interface MultiModelInterface2
{
  @PostConstruct
  default void interface2PostConstruct1()
  {
  }

  @PostConstruct
  default void interface2PostConstruct2()
  {
  }
}
