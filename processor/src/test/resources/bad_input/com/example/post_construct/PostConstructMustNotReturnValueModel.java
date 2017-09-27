package com.example.post_construct;

import javax.annotation.PostConstruct;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class PostConstructMustNotReturnValueModel
{
  @PostConstruct
  int postConstruct()
  {
    return 0;
  }

  @Computed
  public int someValue()
  {
    return 0;
  }
}
