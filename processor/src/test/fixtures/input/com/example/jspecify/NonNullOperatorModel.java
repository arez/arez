package com.example.jspecify;

import arez.annotations.*;
import org.jspecify.annotations.*;

@NullMarked
@ArezComponent(sting = Feature.DISABLE)
abstract class NonNullOperatorModel extends NonNullGenericBase<@Nullable String> {}
