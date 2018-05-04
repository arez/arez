package com.example.package_access.other;

import arez.annotations.Observable;

public abstract class BaseObservable3Model
{
  abstract String getMyValue();

  @Observable
  public abstract void setMyValue( String value );
}
