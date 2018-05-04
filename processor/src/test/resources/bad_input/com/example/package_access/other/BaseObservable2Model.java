package com.example.package_access.other;

import arez.annotations.Observable;

public abstract class BaseObservable2Model
{
  @Observable
  public abstract String getMyValue();

  abstract void setMyValue( String value );
}
