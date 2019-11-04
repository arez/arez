package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

public abstract class UnmanagedObservableComponentReferenceViaInheritanceSuppressedOnBaseClass
{
  @ArezComponent
  public static abstract class MyComponent
    extends Base
  {
  }

  @SuppressWarnings( "Arez:UnmanagedComponentReference" )
  static abstract class Base
  {
    @Observable
    abstract OtherComponent getMyComponent();

    abstract void setMyComponent( OtherComponent component );
  }

  @ArezComponent( allowEmpty = true )
  static abstract class OtherComponent
  {
  }
}
