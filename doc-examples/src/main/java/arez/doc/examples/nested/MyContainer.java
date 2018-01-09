package arez.doc.examples.nested;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

public class MyContainer
{
  @ArezComponent
  public static class MyComponent
  {
    //DOC ELIDE START
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
    //DOC ELIDE END
  }
}
