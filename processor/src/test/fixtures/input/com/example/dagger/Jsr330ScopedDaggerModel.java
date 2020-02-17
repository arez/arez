package com.example.dagger;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import javax.inject.Singleton;

@Singleton
@ArezComponent( sting = Feature.DISABLE, allowEmpty = true )
public abstract class Jsr330ScopedDaggerModel
{
}
