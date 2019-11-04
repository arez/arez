package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
public abstract class ActionTypeParametersModel<T extends Integer>
{
  @Action
  public T doStuff()
  {
    return null;
  }
}
