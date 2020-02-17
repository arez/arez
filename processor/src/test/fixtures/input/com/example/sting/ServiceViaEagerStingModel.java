package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Eager;

@Eager
@ArezComponent( dagger = Feature.DISABLE, allowEmpty = true )
public abstract class ServiceViaEagerStingModel
{
}
