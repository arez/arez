package com.example.post_construct.other;

import arez.annotations.PostConstruct;

public interface MultiModelInterface3
{
  @PostConstruct
  default void interface3PostConstruct1()
  {
  }

  @PostConstruct
  default void interface3PostConstruct2()
  {
  }
}
