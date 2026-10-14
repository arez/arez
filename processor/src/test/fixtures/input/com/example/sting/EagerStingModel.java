package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Eager;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Eager
@ArezComponent( sting = Feature.ENABLE, allowEmpty = true )
public abstract class EagerStingModel
{
}
