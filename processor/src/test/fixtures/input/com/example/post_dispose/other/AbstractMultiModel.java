package com.example.post_dispose.other;

import arez.annotations.PostDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class AbstractMultiModel
{
  @PostDispose
  protected void abstractPostDispose1()
  {
  }

  @PostDispose
  protected void abstractPostDispose2()
  {
  }
}
