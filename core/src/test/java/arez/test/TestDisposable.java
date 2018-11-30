package arez.test;

import arez.Disposable;
import java.util.concurrent.atomic.AtomicInteger;

final class TestDisposable
  implements Disposable
{
  private final AtomicInteger _callCount = new AtomicInteger();
  private boolean _disposed;

  int getCallCount()
  {
    return _callCount.get();
  }

  @Override
  public void dispose()
  {
    _callCount.incrementAndGet();
    if ( !isDisposed() )
    {
      _disposed = true;
    }
  }

  @Override
  public boolean isDisposed()
  {
    return _disposed;
  }
}
