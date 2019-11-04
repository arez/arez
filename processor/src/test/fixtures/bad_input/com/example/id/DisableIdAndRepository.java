package com.example.id;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Repository;

@Repository
@ArezComponent( requireId = Feature.DISABLE )
public abstract class DisableIdAndRepository
{
  @Action
  public void doStuff()
  {
  }
}
