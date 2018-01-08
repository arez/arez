package com.example.name_duplicates;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Autorun;

@ArezComponent
public class ActionAndAutorunMethodModel
{
  @Autorun
  @Action
  public void doStuff()
  {
  }
}
