package com.example.component;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;

public final class NonStaticNestedModel
{
  @ArezComponent
  public class NestedModel
  {
    private long _field;

    @Observable
    public long getField()
    {
      return _field;
    }

    @Observable
    public void setField( final long field )
    {
      _field = field;
    }
  }
}
