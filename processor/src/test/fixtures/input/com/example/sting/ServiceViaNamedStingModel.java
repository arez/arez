package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Named;

@Named( "" )
@ArezComponent( dagger = Feature.DISABLE, allowEmpty = true )
public abstract class ServiceViaNamedStingModel
{
}
