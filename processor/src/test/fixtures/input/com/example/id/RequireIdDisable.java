package com.example.id;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( requireId = Feature.DISABLE, allowEmpty = true )
abstract class RequireIdDisable
{
}
