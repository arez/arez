package com.example.persist;

import arez.annotations.ArezComponent;
import arez.persist.PersistType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ArezComponent
@PersistType
abstract class GenericNullablePersistModel extends GenericPropertyBase<@Nullable String>
{
}
