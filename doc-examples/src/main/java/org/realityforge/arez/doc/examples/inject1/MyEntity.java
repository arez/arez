package org.realityforge.arez.doc.examples.inject1;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.Observable;
import org.realityforge.arez.annotations.Repository;

@ArezComponent
@Repository
public class MyEntity
{
  private int _value;

  @Observable
  public int getValue()
  {
    return _value;
  }

  public void setValue( int value )
  {
    _value = value;
  }
}
