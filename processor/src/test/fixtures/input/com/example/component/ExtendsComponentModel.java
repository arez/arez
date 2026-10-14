package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ExtendsComponentModel
{
  @NullMarked
  @ArezComponent
  public static abstract class MyBaseComponent
  {
    @Action
    public void myAction()
    {
    }
  }

  @NullMarked
  @ArezComponent
  public static abstract class MyComponent
    extends MyBaseComponent
  {
    @Action
    public void myOtherAction()
    {
    }
  }
}
