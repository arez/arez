package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

abstract class UnmanagedObservableComponentReferenceViaInheritance
{
  @ArezComponent
  public static abstract class MyComponent
    extends Base
  {
  }

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
