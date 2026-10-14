package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.LinkType;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent
abstract class EagerLoadNulableObservableReferenceModel
{
  @Reference( load = LinkType.EAGER )
  abstract @Nullable MyEntity getMyEntity();

  @ReferenceId
  @Observable
  @Nullable
  abstract String getMyEntityId();

  abstract void setMyEntityId( @Nullable String id );

  @NullMarked
  static class MyEntity
  {
  }
}
