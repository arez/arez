package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Feature;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class LifecycleManagedNonPrivateServiceField
{
  @CascadeDispose
  @Nullable
  final MyService _myService = null;

  @NullMarked
  @ArezComponent( allowEmpty = true, service = Feature.ENABLE, disposeOnDeactivate = false )
  public abstract static class MyService
  {
  }
}
