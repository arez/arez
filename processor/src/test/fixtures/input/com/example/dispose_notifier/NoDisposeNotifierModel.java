package com.example.dispose_notifier;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( disposeNotifier = Feature.DISABLE )
abstract class NoDisposeNotifierModel
{
  @Action
  public void someValue()
  {
  }
}
