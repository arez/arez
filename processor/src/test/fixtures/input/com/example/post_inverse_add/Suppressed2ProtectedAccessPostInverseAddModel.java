package com.example.post_inverse_add;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.PostInverseAdd;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.annotations.SuppressArezWarnings;
import java.util.Collection;
import javax.annotation.Nonnull;

@ArezComponent
abstract class Suppressed2ProtectedAccessPostInverseAddModel
{
  // This uses the CLASS retention suppression
  @SuppressArezWarnings( "Arez:ProtectedHookMethod" )
  @PostInverseAdd
  protected void postElementsAdd( @Nonnull final Element element )
  {
  }

  @Inverse( referenceName = "other" )
  abstract Collection<Element> getElements();

  @ArezComponent
  static abstract class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract Suppressed2ProtectedAccessPostInverseAddModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
