package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Priority;
import arez.annotations.SuppressArezWarnings;
import org.jspecify.annotations.NullMarked;

// This uses the CLASS retention suppression
@NullMarked
@SuppressArezWarnings( "Arez:UnnecessaryDefaultPriority" )
@ArezComponent( allowEmpty = true, defaultPriority = Priority.LOW )
public abstract class Suppressed2UnnecessaryDefaultPriorityPresentComponent
{
}
