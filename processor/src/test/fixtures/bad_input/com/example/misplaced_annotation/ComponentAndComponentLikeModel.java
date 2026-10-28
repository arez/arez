package com.example.misplaced_annotation;

import arez.annotations.ArezComponent;
import arez.annotations.ArezComponentLike;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ArezComponent( allowEmpty = true )
@ArezComponentLike
abstract class ComponentAndComponentLikeModel
{
}
