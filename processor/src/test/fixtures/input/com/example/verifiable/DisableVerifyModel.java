package com.example.verifiable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent( verify = Feature.DISABLE )
abstract class DisableVerifyModel
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
