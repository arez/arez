package org.realityforge.arez;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IDepTreeNode
{
  @Nonnull
  String getName();

  @Nullable
  ArrayList<IObservable> getObserving();
}
