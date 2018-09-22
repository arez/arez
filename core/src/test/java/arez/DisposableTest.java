package arez;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class DisposableTest
  extends AbstractArezTest
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
  {
    //No exception but no action
    final Object object = new Object();
    assertFalse( Disposable.isDisposed( object ) );
    assertTrue( Disposable.isNotDisposed( object ) );
    Disposable.dispose( object );
    assertFalse( Disposable.isDisposed( object ) );
    assertTrue( Disposable.isNotDisposed( object ) );
  }

  @Test
  public void disposable()
  {
    final TestDisposable object = new TestDisposable();
    assertFalse( object.isDisposed() );
    assertTrue( object.isNotDisposed() );
    assertFalse( Disposable.isDisposed( object ) );
    assertTrue( Disposable.isNotDisposed( object ) );
    Disposable.dispose( object );
    assertTrue( object.isDisposed() );
    assertFalse( object.isNotDisposed() );
    assertTrue( Disposable.isDisposed( object ) );
    assertFalse( Disposable.isNotDisposed( object ) );
  }

  @Test
  public void asDisposable()
  {
    final TestDisposable object = new TestDisposable();
    assertEquals( Disposable.asDisposable( object ), object );

    final Object element = new Object();
    assertInvariantFailure( () -> Disposable.asDisposable( element ),
                            "Object passed to asDisposable does not implement Disposable. Object: " + element );
  }
}
