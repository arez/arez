package com.example.action;

import arez.annotations.Action;
import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class RequiresTxTypeActionModel
{
  @SuppressWarnings( "DefaultAnnotationParam" )
  @Action( requireNewTransaction = false )
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
