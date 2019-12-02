package com.example.post_dispose.other;

import arez.annotations.PostDispose;

public interface MultiModelInterface2
{
  @PostDispose
  default void interface2PostDispose1()
  {
  }

  @PostDispose
  default void interface2PostDispose2()
  {
  }
}
