package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

public final class ExtendsComponentModel
{
  @ArezComponent
  public static abstract class MyBaseComponent
  {
    @Action
    public void myAction()
    {
    }
  }

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
