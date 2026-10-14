package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class NonStaticNestedModel
{
  @NullMarked
  @SuppressWarnings( "InnerClassMayBeStatic" )
  @ArezComponent
  public abstract class NestedModel
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
