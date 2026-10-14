package com.example.post_inverse_add;

import arez.annotations.PostInverseAdd;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
interface PostInverseAddInterface
{
  @PostInverseAdd
  default void postElementsAdd( final PublicAccessViaInterfacePostInverseAddModel.@NonNull Element element )
  {
  }
}
