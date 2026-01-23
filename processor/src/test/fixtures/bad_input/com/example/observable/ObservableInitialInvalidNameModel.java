package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;

@ArezComponent
public abstract class ObservableInitialInvalidNameModel
{
  @Observable
  abstract int getAge();

  abstract void setAge( final int age );

  @ObservableInitial( name = "assert" )
  static int getInitialAge()
  {
    return 5;
  }
}
