package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Eager;
import sting.Typed;

@Typed( {} )
@Eager
@ArezComponent( sting = Feature.ENABLE, allowEmpty = true )
public abstract class EmptyTypedStingModel
{
}
