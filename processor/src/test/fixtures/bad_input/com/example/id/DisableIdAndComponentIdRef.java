package com.example.id;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.ComponentIdRef;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( requireId = Feature.DISABLE )
public abstract class DisableIdAndComponentIdRef
{
  @ComponentIdRef
  abstract int getId();

  @Action
  public void doStuff()
  {
  }
}
