package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Repository;

abstract class NestedModel
{
  @Repository
  @ArezComponent
  public abstract static class BasicActionModel
  {
    @Action
    public void doStuff( final long time )
    {
    }
  }
}
