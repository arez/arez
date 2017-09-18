package com.example.post_construct;

import javax.annotation.PostConstruct;
import org.realityforge.arez.annotations.Computed;
import org.realityforge.arez.annotations.Container;

@Container
public class PostConstructModel
{
  @PostConstruct
  void postConstruct()
  {
  }

  @Computed
  public int someValue()
  {
    return 0;
  }
}
