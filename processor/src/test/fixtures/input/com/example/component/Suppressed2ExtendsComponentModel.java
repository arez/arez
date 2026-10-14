package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Suppressed2ExtendsComponentModel
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
