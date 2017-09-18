package com.example.action;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.Container;

@Container
public class ActionTypeParametersModel<T extends Integer>
{
  @Action
  public T doStuff()
  {
    return null;
  }
}
