package org.realityforge.arez.api2;

import java.util.concurrent.atomic.AtomicInteger;

final class TestAction
  implements Action
{
  private final AtomicInteger _calls = new AtomicInteger();

  int getCalls()
  {
    return _calls.get();
  }

  @Override
  public void call()
    throws Exception
  {
    _calls.incrementAndGet();
  }
}
