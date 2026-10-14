package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
public abstract class ObservableInitialMethodThrowsExceptionModel
{
  @Observable
  abstract int getAge();

  abstract void setAge( final int age );

  @ObservableInitial
  static int getInitialAge()
    throws Exception
  {
    return 1;
  }
}
