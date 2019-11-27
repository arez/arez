package com.example.post_construct;

import arez.annotations.PostConstruct;

interface PostConstructInterface
{
  @PostConstruct
  default void postConstruct()
  {
  }
}
