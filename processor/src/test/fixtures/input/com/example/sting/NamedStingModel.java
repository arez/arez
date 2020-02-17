package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Named;

@Named( "" )
@ArezComponent( sting = Feature.ENABLE, dagger = Feature.DISABLE, allowEmpty = true  )
public abstract class NamedStingModel
{
}
