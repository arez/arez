package arez.component;

import arez.Arez;
import arez.ArezContext;
import arez.Component;
import arez.Disposable;
import arez.Observable;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class EntityReferenceTest
  extends AbstractArezComponentTest
{
  @Test
  public void basicLifecycle()
  {
    final ArezContext context = Arez.context();

    final MyEntity entity1 = new MyEntity( 301 );
    final MyEntity entity2 = new MyEntity( 302 );

    final MyEntityReference reference = MyEntityReference.create();

    final AtomicInteger callCount = new AtomicInteger();
    context.autorun( () -> {
      if ( Disposable.isNotDisposed( reference ) )
      {
        // Observe entity
        reference.getEntity();
      }
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    assertNull( context.safeAction( reference::getEntity ) );
    assertEquals( context.safeAction( reference::hasEntity ), Boolean.FALSE );

    context.safeAction( () -> reference.setEntity( entity1 ) );

    assertEquals( callCount.get(), 2 );

    assertEquals( context.safeAction( reference::getEntity ), entity1 );
    assertEquals( context.safeAction( reference::hasEntity ), Boolean.TRUE );
    assertEquals( Disposable.isDisposed( entity1 ), false );

    context.safeAction( () -> reference.setEntity( null ) );

    assertEquals( callCount.get(), 3 );
    assertEquals( context.safeAction( reference::hasEntity ), Boolean.FALSE );
    assertEquals( Disposable.isDisposed( entity1 ), false );

    context.safeAction( () -> reference.setEntity( entity2 ) );

    assertEquals( callCount.get(), 4 );
    assertEquals( context.safeAction( reference::hasEntity ), Boolean.TRUE );
    assertEquals( Disposable.isDisposed( entity1 ), false );

    Disposable.dispose( reference );

    assertEquals( callCount.get(), 5 );
    assertEquals( Disposable.isDisposed( entity1 ), false );
  }

  @Test
  public void disposeEntityRemovesReference()
  {
    final ArezContext context = Arez.context();

    final MyEntity entity1 = new MyEntity( 301 );

    final MyEntityReference reference = MyEntityReference.create();

    final AtomicInteger callCount = new AtomicInteger();
    context.autorun( () -> {
      if ( Disposable.isNotDisposed( reference ) )
      {
        // Observe entity
        reference.getEntity();
      }
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    assertNull( context.safeAction( reference::getEntity ) );
    assertEquals( context.safeAction( reference::hasEntity ), Boolean.FALSE );

    context.safeAction( () -> reference.setEntity( entity1 ) );

    assertEquals( callCount.get(), 2 );

    assertEquals( context.safeAction( reference::getEntity ), entity1 );
    assertEquals( context.safeAction( reference::hasEntity ), Boolean.TRUE );
    assertEquals( Disposable.isDisposed( entity1 ), false );

    Disposable.dispose( entity1 );

    assertEquals( callCount.get(), 3 );
    assertEquals( context.safeAction( reference::getEntity ), null );
    assertEquals( context.safeAction( reference::hasEntity ), Boolean.FALSE );
  }

  static class MyEntity
    implements Identifiable<Integer>, Disposable, ComponentObservable
  {
    private final Observable<Object> _observable = Arez.context().createObservable();
    private int _arezId;
    private boolean _disposed;

    MyEntity( final int arezId )
    {
      _arezId = arezId;
    }

    @Override
    public boolean observe()
    {
      if ( isDisposed() )
      {
        return false;
      }
      else
      {
        _observable.reportObserved();
        return true;
      }
    }

    @Override
    public void dispose()
    {
      _disposed = true;
      _observable.dispose();
    }

    @Override
    public boolean isDisposed()
    {
      return _disposed;
    }

    @Nonnull
    @Override
    public Integer getArezId()
    {
      return _arezId;
    }
  }

  static class MyEntityReference
    extends AbstractEntityReference<MyEntity>
    implements Disposable
  {
    private final Component _component = Arez.context().createComponent( "MyEntityReference", "1" );
    private final Observable _observable = Arez.context().createObservable();

    static MyEntityReference create()
    {
      return new MyEntityReference();
    }

    @Override
    public boolean isDisposed()
    {
      return _component.isDisposed();
    }

    @Override
    public void dispose()
    {
      Arez.context().dispose( () -> {
        preDispose();
        _component.dispose();
        _observable.dispose();
      } );
    }

    @Nullable
    @Override
    protected MyEntity getEntity()
    {
      _observable.reportObserved();
      return super.getEntity();
    }

    @Override
    protected void setEntity( @Nullable final MyEntity entity )
    {
      _observable.preReportChanged();
      super.setEntity( entity );
      _observable.reportChanged();
    }

    @Override
    @Nonnull
    protected Component component()
    {
      return _component;
    }

    @Nonnull
    protected String getName()
    {
      return "MyRepository";
    }

    @Nonnull
    @Override
    protected ArezContext getContext()
    {
      return Arez.context();
    }
  }
}
