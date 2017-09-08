package org.realityforge.arez.examples;

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
