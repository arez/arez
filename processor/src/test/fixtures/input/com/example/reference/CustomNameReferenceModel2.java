package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class CustomNameReferenceModel2
{
  @Reference( name = "Blah" )
  abstract MyEntity $$$getMyEntity();

  @ReferenceId( name = "Blah" )
  int getMyEntit$$$yId()
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
