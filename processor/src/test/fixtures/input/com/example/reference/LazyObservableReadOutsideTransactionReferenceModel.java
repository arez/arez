package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.LinkType;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class LazyObservableReadOutsideTransactionReferenceModel
{
  @Reference( load = LinkType.LAZY )
  abstract @Nullable MyEntity getMyEntity();

  @ReferenceId
  @Observable( readOutsideTransaction = Feature.ENABLE )
  abstract int getMyEntityId();

  abstract void setMyEntityId( int id );

  @NullMarked
  static class MyEntity
  {
  }
}
