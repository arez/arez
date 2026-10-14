package com.example.post_inverse_add;

import arez.annotations.ArezComponent;
import arez.annotations.PostInverseAdd;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
abstract class MissingInversePostInverseAddModel
{
  @PostInverseAdd
  final void postElementAdd( @NonNull final Object element )
  {
  }
}
