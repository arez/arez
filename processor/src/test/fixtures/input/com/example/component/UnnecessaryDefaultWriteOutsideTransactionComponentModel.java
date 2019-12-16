package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( defaultWriteOutsideTransaction = Feature.DISABLE, allowEmpty = true )
abstract class UnnecessaryDefaultWriteOutsideTransactionComponentModel
{
}
