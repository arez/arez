package org.realityforge.arez;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DisposableTest
{
  static class TestDisposable
    implements Disposable
  {
    private boolean _disposed;

    @Override
    public void dispose()
    {
      _disposed = true;
    }

    public boolean isDisposed()
    {
      return _disposed;
    }
  }

  @Test
  public void dispose_randomObject()
    throws Exception
  {
    //No exception but no action
    Disposable.dispose( new Object() );
  }

  @Test
  public void dispose_disposable()
    throws Exception
  {
    final TestDisposable object = new TestDisposable();
    assertEquals( object.isDisposed(), false );
    Disposable.dispose( object );
    assertEquals( object.isDisposed(), true );
  }
}
