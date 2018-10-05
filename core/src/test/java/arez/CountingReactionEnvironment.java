package arez;

import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;

class CountingReactionEnvironment
  implements ReactionEnvironment
{
  private final AtomicInteger _inEnvironmentCallCount;

  CountingReactionEnvironment( final AtomicInteger inEnvironmentCallCount )
  {
    _inEnvironmentCallCount = inEnvironmentCallCount;
  }

  @Override
  public <T> T run( @Nonnull final SafeFunction<T> function )
  {
    _inEnvironmentCallCount.incrementAndGet();
    return function.call();
  }

  @Override
  public <T> T run( @Nonnull final Function<T> function )
    throws Throwable
  {
    _inEnvironmentCallCount.incrementAndGet();
    return function.call();
  }
}
