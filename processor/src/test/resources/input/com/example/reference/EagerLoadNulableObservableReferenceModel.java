package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.LinkType;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import javax.annotation.Nullable;

@ArezComponent( allowEmpty = true )
abstract class EagerLoadNulableObservableReferenceModel
{
  @Reference( load = LinkType.EAGER )
  abstract MyEntity getMyEntity();

  @ReferenceId
  @Observable
  @Nullable
  abstract String getMyEntityId();

  abstract void setMyEntityId( @Nullable String id );

  static class MyEntity
  {
  }
}
