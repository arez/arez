package com.example.name_duplicates;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Autorun;

@ArezComponent
public class ActionAndAutorunMethodModel
{
  @Autorun
  @Action
  public void doStuff()
  {
  }
}
