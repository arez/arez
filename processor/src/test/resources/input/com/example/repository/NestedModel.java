package com.example.repository;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Repository;

public class NestedModel
{
  @Repository
  @ArezComponent
  public static class BasicActionModel
  {
    @Action
    public void doStuff( final long time )
    {
    }
  }
}
