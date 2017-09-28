package org.realityforge.arez.examples;

import org.realityforge.arez.annotations.Action;
import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Repository;

public class NestedNestedModel
{
  public static class Something
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
}
