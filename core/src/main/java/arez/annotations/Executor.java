package arez.annotations;

import arez.Observer;
import arez.Procedure;

/**
 * Enum describing the agent responsible for executing the {@link arez.Observer}'s observed method.
 */
public enum Executor
{
  /**
   * Arez is responsible for invoking the observed method.
   */
  AREZ,
  /**
   * The application is responsible for invoking the observed method via the {@link arez.ArezContext#observe(Observer, arez.Function, Object...)}  or {@link arez.ArezContext#observe(Observer, Procedure, Object...)} methods.
   */
  APPLICATION
}
