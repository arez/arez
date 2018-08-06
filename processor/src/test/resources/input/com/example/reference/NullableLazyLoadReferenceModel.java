package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.LinkType;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nullable;

@ArezComponent( allowEmpty = true )
abstract class NullableLazyLoadReferenceModel
{
  @Reference( load = LinkType.LAZY )
  abstract MyEntity getMyEntity();

  @ReferenceId
  @Nullable
  Integer getMyEntityId()
  {
    return 0;
  }

  static class MyEntity
  {
  }
}
