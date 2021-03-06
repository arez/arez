package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class NonStandardNameModel
{
  @Inverse( name = "zap", referenceName = "zoom" )
  abstract Collection<Element> getY$$$();

  @ArezComponent
  abstract static class Element
  {
    @Reference( name = "zoom", inverseName = "zap" )
    abstract NonStandardNameModel $$$getX();

    @ReferenceId( name = "zoom" )
    int $$$getXId()
    {
      return 0;
    }
  }
}
