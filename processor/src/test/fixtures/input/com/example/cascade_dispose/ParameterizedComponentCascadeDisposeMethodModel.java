package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ParameterizedComponentCascadeDisposeMethodModel
{
  @CascadeDispose
  @Nullable EventDrivenValue<EventTarget, Integer> myElement()
  {
    return null;
  }

  @NullMarked
  static class EventTarget
  {
  }

  @NullMarked
  @ArezComponent( allowEmpty = true, requireId = Feature.DISABLE, disposeNotifier = Feature.DISABLE )
  abstract static class EventDrivenValue<SourceType extends EventTarget, ValueType extends @Nullable Object>
  {
  }
}
