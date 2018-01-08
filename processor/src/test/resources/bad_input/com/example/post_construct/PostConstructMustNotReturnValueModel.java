package com.example.post_construct;

import arez.annotations.ArezComponent;
import arez.annotations.Computed;
import javax.annotation.PostConstruct;

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
