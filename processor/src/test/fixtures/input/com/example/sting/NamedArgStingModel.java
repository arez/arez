package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Named;

@ArezComponent( sting = Feature.ENABLE, allowEmpty = true )
public abstract class NamedArgStingModel
{
  NamedArgStingModel( @Named( "port" ) int port )
  {
  }
}
