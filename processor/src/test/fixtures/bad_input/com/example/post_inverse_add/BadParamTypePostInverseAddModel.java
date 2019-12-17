package com.example.post_inverse_add;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.PostInverseAdd;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.io.IOException;
import java.util.Collection;
import javax.annotation.Nonnull;

@ArezComponent
abstract class BadParamTypePostInverseAddModel
{
  @PostInverseAdd
  void postElementsAdd( @Nonnull final String element )
  {
  }

  @Inverse( referenceName = "other" )
  abstract Collection<Element> getElements();

  @ArezComponent
  static abstract class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract BadParamTypePostInverseAddModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
