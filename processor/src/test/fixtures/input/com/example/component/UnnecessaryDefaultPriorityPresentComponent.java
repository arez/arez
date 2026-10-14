package com.example.component;

import arez.annotations.ArezComponent;
import arez.annotations.Priority;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true, defaultPriority = Priority.LOW )
public abstract class UnnecessaryDefaultPriorityPresentComponent
{
}
