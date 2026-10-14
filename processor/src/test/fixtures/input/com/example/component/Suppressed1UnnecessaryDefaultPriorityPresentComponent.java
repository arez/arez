package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Priority;
import org.jspecify.annotations.NullMarked;

// This uses the SOURCE retention suppression
@NullMarked
@SuppressWarnings( "Arez:UnnecessaryDefaultPriority" )
@ArezComponent( allowEmpty = true, defaultPriority = Priority.LOW )
public abstract class Suppressed1UnnecessaryDefaultPriorityPresentComponent
{
}
