package arez;

import grim.annotations.OmitType;
import javax.annotation.Nonnull;

/**
 * An interceptor that intercepts the execution of tasks.
 * This enables downstream the injection of logic before and after tasks have been scheduled.
 */
@OmitType( unless = "arez.enable_task_interceptor" )
@FunctionalInterface
public interface TaskInterceptor
{
  /**
   * Method invoked to execute tasks.
   *
   * @param executeAction action to invoke to execute tasks.
   */
  void executeTasks( @Nonnull SafeProcedure executeAction );
}
