package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( sting = Feature.ENABLE, allowEmpty = true, dagger = Feature.DISABLE )
public abstract class ParameterizedTypeStingModel<T>
{
}
