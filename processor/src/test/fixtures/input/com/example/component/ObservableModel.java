package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( observable = Feature.ENABLE )
abstract class ObservableModel
{
  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
