package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Eager;
import sting.Typed;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Typed( ServiceViaTypedStingModel.class )
@ArezComponent( allowEmpty = true )
public abstract class ServiceViaTypedStingModel
{
}
