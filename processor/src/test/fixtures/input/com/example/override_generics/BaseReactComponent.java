package com.example.override_generics;

import java.util.AbstractList;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class BaseReactComponent<P extends AbstractList<?>, S extends AbstractList<?>>
{
}
