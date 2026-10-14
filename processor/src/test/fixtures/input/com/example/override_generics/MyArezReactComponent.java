package com.example.override_generics;

import java.util.ArrayList;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class MyArezReactComponent
  extends ArezReactComponent<ArrayList<?>>
{
  @Override
  protected void reportPropsChanged( @Nullable final ArrayList<?> nextProps )
  {
  }
}
