package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent
abstract class UnmanagedObservableComponentReferenceToNonVerify
{
  @Observable
  abstract MyComponent getMyComponent();

  abstract void setMyComponent( MyComponent component );

  @ArezComponent( allowEmpty = true, verifyReferencesToComponent = Feature.DISABLE )
  public abstract static class MyComponent
  {
  }
}
