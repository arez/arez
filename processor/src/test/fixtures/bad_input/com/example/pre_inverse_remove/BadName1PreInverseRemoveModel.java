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
abstract class BadName1PreInverseRemoveModel
{
  @PreInverseRemove( name = "-e-e-" )
  void preElementsRemove( @Nonnull final Element element )
  {
  }

  @Inverse( referenceName = "other" )
  abstract Collection<Element> getElements();

  @ArezComponent
  static abstract class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract BadName1PreInverseRemoveModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
