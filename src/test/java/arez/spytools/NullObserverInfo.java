package arez.spytools;

import arez.Priority;
import arez.spy.ComponentInfo;
import arez.spy.ComputedValueInfo;
import arez.spy.ObservableValueInfo;
import arez.spy.ObserverInfo;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.realityforge.guiceyloops.shared.ValueUtil;

final class NullObserverInfo
  implements ObserverInfo
{
  @Override
  public boolean isRunning()
  {
    return false;
  }

  @Override
  public boolean isScheduled()
  {
    return false;
  }

  @Override
  public boolean isComputedValue()
  {
    return false;
  }

  @Nonnull
  @Override
  public Priority getPriority()
  {
    return Priority.NORMAL;
  }

  @Override
  public boolean isReadOnly()
  {
    return false;
  }

  @Override
  public ComputedValueInfo asComputedValue()
  {
    return null;
  }

  @Nonnull
  @Override
  public List<ObservableValueInfo> getDependencies()
  {
    return Collections.emptyList();
  }

  @Nullable
  @Override
  public ComponentInfo getComponent()
  {
    return null;
  }

  @Nonnull
  @Override
  public String getName()
  {
    return ValueUtil.randomString();
  }

  @Override
  public boolean isDisposed()
  {
    return false;
  }
}
