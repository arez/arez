package com.example.pre_inverse_remove;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.PreInverseRemove;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;
import javax.annotation.Nonnull;

@ArezComponent
abstract class Suppressed1PublicAccessPreInverseRemoveModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:PublicHookMethod" )
  @PreInverseRemove
  public void preElementsRemove( @Nonnull final Element element )
  {
  }

  @Inverse( referenceName = "other" )
  abstract Collection<Element> getElements();

  @ArezComponent
  abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract Suppressed1PublicAccessPreInverseRemoveModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
