package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Typed;

@Typed( { Object.class, TypedStingModel.class } )
@ArezComponent( sting = Feature.ENABLE, dagger = Feature.DISABLE, allowEmpty = true )
public abstract class TypedStingModel
{
}
