package org.realityforge.arez;

import javax.annotation.Nonnull;

public final class ArezTestUtil
{
  private ArezTestUtil()
  {
  }

  public static boolean isActive( @Nonnull final Observer observer )
  {
    return observer.isActive();
  }
}
