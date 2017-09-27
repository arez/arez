package com.example.component_id;

import org.realityforge.arez.annotations.ArezComponent;
import org.realityforge.arez.annotations.ComponentId;
import org.realityforge.arez.annotations.Observable;

@ArezComponent
public class ComponentIdDuplicatedModel
{
  @ComponentId
  final long getId()
  {
    return 0;
  }

  @ComponentId
  final long getOtherId()
  {
    return 0;
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
