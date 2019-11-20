package com.example.component;

import arez.annotations.Action;
import arez.annotations.ArezComponent;

@ArezComponent
abstract class PublicCtorNonPublicModel
{
  @SuppressWarnings( "WeakerAccess" )
  public PublicCtorNonPublicModel()
  {
  }

  @Action
  public void doStuff( final long time, float someOtherParameter )
  {
  }
}
