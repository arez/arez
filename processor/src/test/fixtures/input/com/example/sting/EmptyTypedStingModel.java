package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Eager;
import sting.Typed;

@Typed( {} )
@Eager
@ArezComponent( sting = Feature.ENABLE, dagger = Feature.DISABLE, allowEmpty = true )
public abstract class EmptyTypedStingModel
{
}
