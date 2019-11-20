package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Priority;
import arez.annotations.SuppressArezWarnings;

// This uses the CLASS retention suppression
@SuppressArezWarnings( "Arez:UnnecessaryDefaultPriority" )
@ArezComponent( allowEmpty = true, defaultPriority = Priority.LOW )
public abstract class Suppressed2UnnecessaryDefaultPriorityPresentComponent
{
}
