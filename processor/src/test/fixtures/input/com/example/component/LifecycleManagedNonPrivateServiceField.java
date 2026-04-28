package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Feature;
import javax.annotation.Nullable;

@ArezComponent
abstract class LifecycleManagedNonPrivateServiceField
{
  @CascadeDispose
  @Nullable
  final MyService _myService = null;

  @ArezComponent( allowEmpty = true, service = Feature.ENABLE, disposeOnDeactivate = false )
  public abstract static class MyService
  {
  }
}
