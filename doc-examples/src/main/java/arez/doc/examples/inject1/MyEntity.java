package arez.doc.examples.inject1;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;
import arez.annotations.Repository;

@ArezComponent
@Repository
public abstract class MyEntity
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
