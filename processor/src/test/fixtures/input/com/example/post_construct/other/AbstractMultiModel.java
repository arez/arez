package com.example.post_construct.other;

import arez.annotations.PostConstruct;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class AbstractMultiModel
{
  @PostConstruct
  protected void abstractPostConstruct1()
  {
  }

  @PostConstruct
  protected void abstractPostConstruct2()
  {
  }
}
