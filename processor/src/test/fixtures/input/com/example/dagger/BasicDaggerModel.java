package com.example.dagger;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( dagger = Feature.ENABLE, sting = Feature.DISABLE, allowEmpty = true )
public abstract class BasicDaggerModel
{
}
