package com.example.observable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;

@ArezComponent
abstract class WriteOutsideTransactionThrowingObservablesModel
{
  private long _time;

  @Observable( writeOutsideTransaction = Feature.ENABLE )
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
