package com.example.id;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( requireId = Feature.ENABLE, allowEmpty = true )
abstract class RequireIdEnable
{
}
