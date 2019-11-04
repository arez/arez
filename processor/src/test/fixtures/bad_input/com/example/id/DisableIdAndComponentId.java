package com.example.id;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Feature;

@ArezComponent( requireId = Feature.DISABLE )
public abstract class DisableIdAndComponentId
{
  @ComponentId
  final int getId()
  {
    return 0;
  }

  @Action
  public void doStuff()
  {
  }
}
