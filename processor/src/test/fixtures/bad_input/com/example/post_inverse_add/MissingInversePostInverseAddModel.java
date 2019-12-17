package com.example.post_inverse_add;

import arez.annotations.ArezComponent;
import arez.annotations.PostInverseAdd;
import javax.annotation.Nonnull;

@ArezComponent( allowEmpty = true )
abstract class MissingInversePostInverseAddModel
{
  @PostInverseAdd
  final void postElementAdd( @Nonnull final Object element )
  {
  }
}
