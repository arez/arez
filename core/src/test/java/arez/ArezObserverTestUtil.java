package arez;

import javax.annotation.Nonnull;

public final class ArezObserverTestUtil
{
  private ArezObserverTestUtil()
  {
  }

  public static boolean isActive( @Nonnull final Node observer )
  {
    return ( (Observer) observer ).isActive();
  }
}
