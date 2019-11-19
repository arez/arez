package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
abstract class NewTypeParametersModel
{
  @Action
  public <T extends Integer> T doStuff()
  {
    return null;
  }
}
