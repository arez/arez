package com.example.sting;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.Named;

@Named( "" )
@ArezComponent( sting = Feature.DISABLE, allowEmpty = true )
public abstract class NamedButNoStingModel
{
}
