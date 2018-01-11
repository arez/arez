package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( inject = Feature.ENABLE )
public class DefaultCtorModel
{
  @Action
  public void myActionStuff()
  {
  }
}
