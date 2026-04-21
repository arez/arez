package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Feature;

@ArezComponent
abstract class LifecycleManagedNonPrivateServiceField
{
  @CascadeDispose
  final MyService _myService = null;

  @ArezComponent( allowEmpty = true, service = Feature.ENABLE, disposeOnDeactivate = false )
  public abstract static class MyService
  {
  }
}
