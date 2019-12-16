package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.LinkType;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class EagerObservableReadOutsideTransactionReferenceModel
{
  @Reference( load = LinkType.EAGER )
  abstract MyEntity getMyEntity();

  @ReferenceId
  @Observable( readOutsideTransaction = Feature.ENABLE )
  abstract int getMyEntityId();

  abstract void setMyEntityId( int id );

  static class MyEntity
  {
  }
}
