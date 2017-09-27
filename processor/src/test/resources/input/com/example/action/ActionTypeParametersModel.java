package com.example.action;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@ArezComponent
public class ActionTypeParametersModel<T extends Integer>
{
  @Action
  public T doStuff()
  {
    return null;
  }
}
