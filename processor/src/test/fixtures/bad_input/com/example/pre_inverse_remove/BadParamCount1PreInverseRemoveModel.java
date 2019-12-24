package com.example.pre_inverse_remove;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.PreInverseRemove;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;

@ArezComponent
abstract class BadParamCount1PreInverseRemoveModel
{
  @PreInverseRemove
  void preElementsRemove()
  {
  }

  @Inverse( referenceName = "other" )
  abstract Collection<Element> getElements();

  @ArezComponent
  abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract BadParamCount1PreInverseRemoveModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
