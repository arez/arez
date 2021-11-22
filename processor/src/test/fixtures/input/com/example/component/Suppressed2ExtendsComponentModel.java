package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.SuppressArezWarnings;

public final class Suppressed2ExtendsComponentModel
{
  @ArezComponent
  public static abstract class MyBaseComponent
  {
    @Action
    public void myAction()
    {
    }
  }

  @SuppressArezWarnings( "Arez:ExtendsComponent" )
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
