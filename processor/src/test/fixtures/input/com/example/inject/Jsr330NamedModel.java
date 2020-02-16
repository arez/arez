package com.example.inject;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import javax.inject.Named;

@Named
@ArezComponent
public abstract class Jsr330NamedModel
{
  @Action
  public void myActionStuff()
  {
  }
}
