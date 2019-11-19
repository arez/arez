package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
abstract class WriteOutsideTransactionThrowingObservablesModel
{
  private long _time;

  @Observable( writeOutsideTransaction = true )
  public long getTime()
  {
    return _time;
  }

  public void setTime( long value )
    throws Exception
  {
    _time = value;
  }
}
