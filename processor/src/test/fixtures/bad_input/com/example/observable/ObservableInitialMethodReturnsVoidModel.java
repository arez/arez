package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.ObservableInitial;

@ArezComponent
public abstract class ObservableInitialMethodReturnsVoidModel
{
  @Observable
  abstract int getAge();

  abstract void setAge( final int age );

  @ObservableInitial
  static void getInitialAge()
  {
  }
}
