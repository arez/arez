package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Repository;

abstract class NestedModel
{
  @Repository
  @ArezComponent
  public static abstract class BasicActionModel
  {
    @Action
    public void doStuff( final long time )
    {
    }
  }
}
