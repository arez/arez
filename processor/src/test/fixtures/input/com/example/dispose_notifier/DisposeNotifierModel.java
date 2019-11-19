package com.example.dispose_notifier;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( disposeNotifier = Feature.ENABLE )
abstract class DisposeNotifierModel
{
  @Action
  public void someValue()
  {
  }
}
