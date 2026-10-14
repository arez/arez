package com.example.action;

import arez.annotations.ArezComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
public abstract class JaxWsActionModel
{
  @javax.xml.ws.Action
  void setField()
  {
  }
}
