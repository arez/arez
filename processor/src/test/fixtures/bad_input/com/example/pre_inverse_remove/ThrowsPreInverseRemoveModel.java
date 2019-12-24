package com.example.pre_inverse_remove;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.PreInverseRemove;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.io.IOException;
import java.util.Collection;
import javax.annotation.Nonnull;

@ArezComponent
abstract class ThrowsPreInverseRemoveModel
{
  @PreInverseRemove
  void preElementsRemove( @Nonnull final Element element )
    throws IOException
  {
    throw new IOException();
  }

  @Inverse( referenceName = "other" )
  abstract Collection<Element> getElements();

  @ArezComponent
  abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract ThrowsPreInverseRemoveModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
