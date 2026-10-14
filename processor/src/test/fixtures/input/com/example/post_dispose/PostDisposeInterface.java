package com.example.post_dispose;

import arez.annotations.PostDispose;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PostDisposeInterface
{
  @PostDispose
  default void postDispose()
  {
  }
}
