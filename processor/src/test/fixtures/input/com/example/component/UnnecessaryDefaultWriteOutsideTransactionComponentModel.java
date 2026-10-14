package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( defaultWriteOutsideTransaction = Feature.DISABLE, allowEmpty = true )
abstract class UnnecessaryDefaultWriteOutsideTransactionComponentModel
{
}
