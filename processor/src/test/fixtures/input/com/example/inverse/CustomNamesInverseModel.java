package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class CustomNamesInverseModel
{
  @Inverse( name = "zap", referenceName = "zoom" )
  abstract Collection<Element> getY();

  @ArezComponent
  static abstract class Element
  {
    @Reference( name = "zoom", inverseName = "zap" )
    abstract CustomNamesInverseModel getX();

    @ReferenceId( name = "zoom" )
    int getXId()
    {
      return 0;
    }
  }
}
