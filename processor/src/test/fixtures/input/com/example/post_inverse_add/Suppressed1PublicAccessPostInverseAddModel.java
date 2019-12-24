package com.example.post_inverse_add;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Inverse;
import arez.annotations.PostInverseAdd;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import java.util.Collection;
import javax.annotation.Nonnull;

@ArezComponent
abstract class Suppressed1PublicAccessPostInverseAddModel
{
  // This uses the SOURCE retention suppression
  @SuppressWarnings( "Arez:PublicHookMethod" )
  @PostInverseAdd
  public void postElementsAdd( @Nonnull final Element element )
  {
  }

  @Inverse( referenceName = "other" )
  abstract Collection<Element> getElements();

  @ArezComponent
  abstract static class Element
  {
    @Reference( inverse = Feature.ENABLE )
    abstract Suppressed1PublicAccessPostInverseAddModel getOther();

    @ReferenceId
    int getOtherId()
    {
      return 0;
    }
  }
}
