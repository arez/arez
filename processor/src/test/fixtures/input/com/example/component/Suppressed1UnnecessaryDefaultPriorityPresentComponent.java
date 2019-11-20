package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Priority;

// This uses the SOURCE retention suppression
@SuppressWarnings( "Arez:UnnecessaryDefaultPriority" )
@ArezComponent( allowEmpty = true, defaultPriority = Priority.LOW )
public abstract class Suppressed1UnnecessaryDefaultPriorityPresentComponent
{
}
