package arez.doc.examples.observables;

import arez.ObservableValue;

public class ObservableExample
{
  private ObservableValue<Integer> _valueObservableValue;
  private int _value;

  public int getValue()
  {
    _valueObservableValue.reportObserved();
    return _value;
  }

  public void setValue( final int value )
  {
    if ( value != _value )
    {
      _value = value;
      _valueObservableValue.reportChanged();
    }
  }
}
