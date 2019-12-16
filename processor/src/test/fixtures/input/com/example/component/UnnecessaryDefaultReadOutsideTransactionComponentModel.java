package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;

@ArezComponent( defaultReadOutsideTransaction = Feature.DISABLE, allowEmpty = true )
abstract class UnnecessaryDefaultReadOutsideTransactionComponentModel
{
}
