package com.example.post_construct;

import javax.annotation.PostConstruct;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class PostConstructNotPrivateModel
{
  @PostConstruct
  private void postConstruct()
  {
  }

  @Computed
  public int someValue()
  {
    return 0;
  }
}
