package arez.doc.examples.access_level;

import arez.annotations.ArezComponent;
import arez.annotations.Observable;

@ArezComponent
public abstract class MyComponent
{
  public static MyComponent create()
  {
    return new Arez_MyComponent();
  }

  MyComponent()
  {
  }
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
