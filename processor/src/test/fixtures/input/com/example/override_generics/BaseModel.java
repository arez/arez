package com.example.override_generics;

import javax.annotation.Nonnull;

abstract class BaseModel<T>
{
  protected abstract void myAbstractMethod( @Nonnull T value );
}
