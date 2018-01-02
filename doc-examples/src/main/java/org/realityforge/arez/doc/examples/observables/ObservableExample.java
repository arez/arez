package org.realityforge.arez.doc.examples.observables;

import org.realityforge.arez.Observable;

public class ObservableExample
{
  private Observable<Integer> _valueObservable;
  private int _value;

  public int getValue()
  {
    _valueObservable.reportObserved();
    return _value;
  }

  public void setValue( final int value )
  {
    if ( value != _value )
    {
      _value = value;
      _valueObservable.reportChanged();
    }
  }
}
