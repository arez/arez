package com.example.post_inverse_add;

import arez.annotations.PostInverseAdd;
import javax.annotation.Nonnull;

interface PostInverseAddInterface
{
  @PostInverseAdd
  default void postElementsAdd( @Nonnull final PublicAccessViaInterfacePostInverseAddModel.Element element )
  {
  }
}
