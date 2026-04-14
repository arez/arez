package com.example.component;

import arez.annotations.ActAsComponent;
import arez.annotations.ArezComponent;
import arez.annotations.AutoObserve;

@ArezComponent
abstract class ManagedAutoObserveActAsComponentReference
{
  @AutoObserve( validateTypeAtRuntime = true )
  final MyComponent _myComponent = null;

  @ActAsComponent
  public interface MyComponent
  {
  }
}
