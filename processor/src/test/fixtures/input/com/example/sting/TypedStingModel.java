package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Typed;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Typed( { Object.class, TypedStingModel.class } )
@ArezComponent( sting = Feature.ENABLE, allowEmpty = true )
public abstract class TypedStingModel
{
}
