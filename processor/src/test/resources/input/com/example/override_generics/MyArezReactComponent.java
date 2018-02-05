package com.example.override_generics;

import java.util.ArrayList;
import javax.annotation.Nullable;

public abstract class MyArezReactComponent
  extends ArezReactComponent<ArrayList>
{
  @Override
  protected void reportPropsChanged( @Nullable final ArrayList nextProps )
  {
  }
}
