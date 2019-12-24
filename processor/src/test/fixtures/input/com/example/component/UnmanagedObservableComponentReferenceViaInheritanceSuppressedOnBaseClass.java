package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

abstract class UnmanagedObservableComponentReferenceViaInheritanceSuppressedOnBaseClass
{
  @ArezComponent
  public abstract static class MyComponent
    extends Base
  {
  }

  @SuppressWarnings( "Arez:UnmanagedComponentReference" )
  abstract static class Base
  {
    @Observable
    abstract OtherComponent getMyComponent();

    abstract void setMyComponent( OtherComponent component );
  }

  @ArezComponent( allowEmpty = true )
  abstract static class OtherComponent
  {
  }
}
