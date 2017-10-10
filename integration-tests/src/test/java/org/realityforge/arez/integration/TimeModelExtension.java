package org.realityforge.arez.integration;

import org.realityforge.arez.annotations.Action;

public interface TimeModelExtension
{
  TimeModel self();

  @Action
  default void resetTime()
  {
    self().setTime( 0 );
  }
}
