package com.example.override_generics;

import java.util.AbstractList;
import java.util.ArrayList;
import javax.annotation.Nullable;

public abstract class ArezReactComponent<P extends AbstractList>
  extends BaseReactComponent<P, ArrayList>
{
  protected abstract void reportPropsChanged( @Nullable final P nextProps );
}
