package com.example.dagger;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import javax.inject.Named;

@ArezComponent( dagger = Feature.ENABLE, sting = Feature.DISABLE, allowEmpty = true )
public abstract class Jsr330NamedArgDaggerModel
{
  Jsr330NamedArgDaggerModel( @Named( "Port" ) int port )
  {
  }
}
