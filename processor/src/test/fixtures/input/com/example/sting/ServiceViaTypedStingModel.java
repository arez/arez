package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Eager;
import sting.Typed;

@Typed( ServiceViaTypedStingModel.class )
@ArezComponent( dagger = Feature.DISABLE, allowEmpty = true )
public abstract class ServiceViaTypedStingModel
{
}
