package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Named;

@ArezComponent( sting = Feature.DISABLE, allowEmpty = true )
public abstract class NamedArgButNoStingModel
{
  NamedArgButNoStingModel( @Named( "port" ) int port )
  {
  }
}
