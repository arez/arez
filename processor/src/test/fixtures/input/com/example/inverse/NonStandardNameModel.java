package com.example.inverse;

import arez.annotations.ArezComponent;
import arez.annotations.Inverse;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class NonStandardNameModel
{
  @Inverse( name = "zap", referenceName = "zoom" )
  abstract @Nullable Collection<Element> getY$$$();

  @NullMarked
  @ArezComponent
  abstract static class Element
  {
    @Reference( name = "zoom", inverseName = "zap" )
    abstract @Nullable NonStandardNameModel $$$getX();

    @ReferenceId( name = "zoom" )
    int $$$getXId()
    {
      return 0;
    }
  }
}
