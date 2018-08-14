package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.LinkType;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;

@ArezComponent
abstract class ExplicitLoadObservableReferenceModel
{
  @Reference( load = LinkType.EXPLICIT )
  abstract MyEntity getMyEntity();

  @ReferenceId
  @Observable
  abstract int getMyEntityId();

  abstract void setMyEntityId( int id );

  static class MyEntity
  {
  }
}
