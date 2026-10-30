package com.example.mapped_relationship;

import arez.annotations.ArezComponentLike;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponentLike
abstract class ContextBase extends GenericContextBase<@Nullable String> {
  ContextBase() { super(null); }
}
