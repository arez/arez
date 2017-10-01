package org.realityforge.arez.spy;

import javax.annotation.Nonnull;

final class EventUtil
{
  private EventUtil()
  {
  }

  @Nonnull
  static String getName( @Nonnull final Class<?> type )
  {
    return type.getSimpleName().replaceAll( "Event$", "" );
  }
}
