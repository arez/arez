package com.example.verifiable;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( verify = Feature.ENABLE, allowEmpty = true )
abstract class EnableVerifyModel
{
}
