package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.ObservableInitial;

@ArezComponent
public abstract class ObservableInitialNoMatchingObservableModel
{
  @ObservableInitial
  static int getInitialAge()
  {
    return 1;
  }
}
