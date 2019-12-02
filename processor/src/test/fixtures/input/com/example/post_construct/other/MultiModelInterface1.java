package com.example.post_construct.other;

import arez.annotations.PostConstruct;

public interface MultiModelInterface1
{
  @PostConstruct
  default void interface1PostConstruct1()
  {
  }

  @PostConstruct
  default void interface1PostConstruct2()
  {
  }
}
