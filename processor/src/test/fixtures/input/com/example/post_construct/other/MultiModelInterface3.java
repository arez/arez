package com.example.post_construct.other;

import arez.annotations.PostConstruct;
import org.jspecify.annotations.NullMarked;

@NullMarked
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
