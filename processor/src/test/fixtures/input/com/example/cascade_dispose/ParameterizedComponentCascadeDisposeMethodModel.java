package com.example.cascade_dispose;

import arez.annotations.ArezComponent;
import arez.annotations.CascadeDispose;
import arez.annotations.Feature;

@ArezComponent
abstract class ParameterizedComponentCascadeDisposeMethodModel
{
  @CascadeDispose
  EventDrivenValue<EventTarget, Integer> myElement()
  {
    return null;
  }

  static class EventTarget
  {
  }

  @ArezComponent( allowEmpty = true, requireId = Feature.DISABLE, disposeNotifier = Feature.DISABLE )
  abstract static class EventDrivenValue<SourceType extends EventTarget, ValueType>
  {
  }
}
