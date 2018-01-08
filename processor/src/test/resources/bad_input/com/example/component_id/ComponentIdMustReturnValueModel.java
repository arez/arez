package com.example.component_id;

import arez.annotations.ArezComponent;
import arez.annotations.ComponentId;
import arez.annotations.Observable;

@ArezComponent
public class ComponentIdMustReturnValueModel
{
  @ComponentId
  final void getId()
  {
  }

  @Observable
  public long getField()
  {
    return 0;
  }

  @Observable
  public void setField( final long field )
  {
  }
}
