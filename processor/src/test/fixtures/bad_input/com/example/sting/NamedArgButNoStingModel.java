package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Named;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( sting = Feature.DISABLE, allowEmpty = true )
public abstract class NamedArgButNoStingModel
{
  NamedArgButNoStingModel( @Named( "port" ) int port )
  {
  }
}
