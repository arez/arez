package com.example.sting.autofragment;

import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import sting.ContributeTo;

@ContributeTo( "MyAutoFragment" )
@ArezComponent( dagger = Feature.DISABLE, allowEmpty = true )
public abstract class ServiceViaContributeToStingModel
{
}
