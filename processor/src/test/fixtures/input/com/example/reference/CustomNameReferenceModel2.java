package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class CustomNameReferenceModel2
{
  @Reference( name = "Blah" )
  abstract @Nullable MyEntity $$$getMyEntity();

  @ReferenceId( name = "Blah" )
  int getMyEntit$$$yId()
  {
    return 0;
  }

  @NullMarked
  static class MyEntity
  {
  }
}
