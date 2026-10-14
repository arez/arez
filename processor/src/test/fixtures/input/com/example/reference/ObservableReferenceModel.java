package com.example.reference;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
abstract class ObservableReferenceModel
{
  @Reference
  abstract @Nullable MyEntity getMyEntity();

  @ReferenceId
  @Observable
  abstract int getMyEntityId();

  abstract void setMyEntityId( int id );

  @NullMarked
  static class MyEntity
  {
  }
}
