package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Eager;

@Eager
@ArezComponent( sting = Feature.ENABLE, allowEmpty = true )
public abstract class EagerStingModel
{
}
