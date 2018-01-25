package arez.doc.examples.at_observable;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class MyModel
{
  private int _value;

  @Observable
  public int getValue()
  {
    return _value;
  }

  public void setValue( final int value )
  {
    _value = value;
  }
}
