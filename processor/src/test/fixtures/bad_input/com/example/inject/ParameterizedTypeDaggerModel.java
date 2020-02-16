package com.example.inject;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( dagger = Feature.ENABLE, allowEmpty = true )
public abstract class ParameterizedTypeDaggerModel<T>
{
}
