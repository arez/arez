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

    @Override
    public boolean isDisposed()
    {
      return _disposed;
    }
  }

  @Test
  public void randomObject()
    throws Exception
  {
    //No exception but no action
    final Object object = new Object();
    assertEquals( Disposable.isDisposed( object ), false );
    Disposable.dispose( object );
    assertEquals( Disposable.isDisposed( object ), false );
  }

  @Test
  public void disposable()
    throws Exception
  {
    final TestDisposable object = new TestDisposable();
    assertEquals( object.isDisposed(), false );
    assertEquals( Disposable.isDisposed( object ), false );
    Disposable.dispose( object );
    assertEquals( object.isDisposed(), true );
    assertEquals( Disposable.isDisposed( object ), true );
  }
}
