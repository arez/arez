package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.ObservableInitial;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ObservableInitialNoMatchingObservableModel
{
  @ObservableInitial
  static int getInitialAge()
  {
    return 1;
  }
}
