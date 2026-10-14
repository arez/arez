package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Named;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Named( "" )
@ArezComponent( sting = Feature.ENABLE, allowEmpty = true  )
public abstract class NamedStingModel
{
}
