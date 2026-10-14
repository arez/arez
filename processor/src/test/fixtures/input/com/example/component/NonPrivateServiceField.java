package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class NonPrivateServiceField
{
  final @Nullable MyService _myService = null;

  @NullMarked
  @ArezComponent( allowEmpty = true, service = Feature.ENABLE )
  public abstract static class MyService
  {
  }
}
