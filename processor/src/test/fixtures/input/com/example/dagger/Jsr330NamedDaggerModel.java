package com.example.dagger;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import javax.inject.Named;

@Named
@ArezComponent( sting = Feature.DISABLE, allowEmpty = true )
public abstract class Jsr330NamedDaggerModel
{
}
