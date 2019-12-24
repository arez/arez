package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public enum EnumModel
{
  A, B;

  @Observable
  public long getField()
  {
    return 0;
  }

  @Observable
  public void setField( final long field )
  {
  }
}
