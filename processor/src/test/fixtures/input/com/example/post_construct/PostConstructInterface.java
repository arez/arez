package com.example.post_construct;

import arez.annotations.PostConstruct;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface PostConstructInterface
{
  @PostConstruct
  default void postConstruct()
  {
  }
}
