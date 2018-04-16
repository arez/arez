package com.example.package_access.other;

import arez.annotations.Observable;

public abstract class BaseObservableModel
{
  @Observable
  abstract String getMyValue();

  public abstract void setMyValue( String value );
}
