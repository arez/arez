package com.example.inject;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( dagger = Feature.ENABLE, sting = Feature.ENABLE, allowEmpty = true )
public abstract class DualNamedArgInjectModel
{
  DualNamedArgInjectModel( @javax.inject.Named( "Port" ) @sting.Named( "Port" ) int port )
  {
  }
}
