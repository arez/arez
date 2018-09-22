package arez;

import java.util.concurrent.atomic.AtomicInteger;

final class TestProcedure
  implements Procedure
{
  private final AtomicInteger _calls = new AtomicInteger();

  int getCalls()
  {
    return _calls.get();
  }

  @Override
  public void call()
  {
    _calls.incrementAndGet();
  }
}
