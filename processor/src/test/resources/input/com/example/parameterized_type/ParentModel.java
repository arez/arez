package com.example.parameterized_type;

import org.realityforge.arez.annotations.Observable;

class ParentModel<T extends Number>
{
  private T _value;

  @Observable
  public T getValue()
  {
    return _value;
  }

  public void setValue( final T value )
  {
    _value = value;
  }
}
