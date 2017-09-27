package com.example.action;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;

@ArezComponent
public class NewTypeParametersModel
{
  @Action
  public <T extends Integer> T doStuff()
  {
    return null;
  }
}
