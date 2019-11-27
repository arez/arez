package com.example.post_dispose;

import arez.annotations.PostDispose;

public interface PostDisposeInterface
{
  @PostDispose
  default void postDispose()
  {
  }
}
