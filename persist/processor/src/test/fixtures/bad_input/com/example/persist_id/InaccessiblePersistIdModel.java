package com.example.persist_id;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.persist.Persist;
import arez.persist.PersistType;
import com.example.persist_id.other.BasePackageAccessPersistIdModel;

@PersistType
@ArezComponent
abstract class InaccessiblePersistIdModel
  extends BasePackageAccessPersistIdModel
{
  @Observable
  @Persist
  public abstract int getValue();

  public abstract void setValue( int v );
}
