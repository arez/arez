package com.example.post_construct.other;

import arez.annotations.PostConstruct;
import org.jspecify.annotations.NullMarked;

@NullMarked
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
