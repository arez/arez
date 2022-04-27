package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Typed;

@Typed( Object.class )
@ArezComponent( sting = Feature.DISABLE, allowEmpty = true )
public abstract class TypedButNoStingModel
{
}
