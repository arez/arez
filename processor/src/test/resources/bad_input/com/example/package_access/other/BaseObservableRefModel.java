package com.example.package_access.other;

import arez.annotations.Observable;
import arez.annotations.ObservableRef;
import javax.annotation.Nonnull;

public abstract class BaseObservableRefModel
{
  @Observable
  public abstract String getMyValue();

  public abstract void setMyValue( String value );

  @Nonnull
  @ObservableRef
  abstract arez.Observable<String> getMyValueObservable();
}
