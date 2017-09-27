package com.example.post_construct;

import javax.annotation.PostConstruct;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Computed;

@ArezComponent
public class PostConstructDuplicateModel
{
  @Computed
  public int getMyValue()
  {
    return 0;
  }

  @PostConstruct
  void postConstruct1()
  {
  }

  @PostConstruct
  void postConstruct2()
  {
  }
}
