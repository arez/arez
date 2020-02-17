package com.example.dagger;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( dagger = Feature.ENABLE, allowEmpty = true, sting = Feature.DISABLE )
public abstract class ParameterizedTypeDaggerModel<T>
{
}
