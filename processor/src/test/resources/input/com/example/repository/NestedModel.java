package com.example.repository;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Repository;

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
