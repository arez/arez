package arez.component.internal;

import arez.AbstractTest;
import arez.Arez;
import arez.ArezContext;
import arez.Disposable;
import arez.ObservableValue;
import arez.SafeProcedure;
import arez.component.ComponentObservable;
import arez.component.DisposeNotifier;
import arez.component.Identifiable;
import arez.component.NoSuchEntityException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ContainerTest
  extends AbstractTest
{
  @Test
  public void entityLifecycles()
  {
    final ArezContext context = Arez.context();

    final MyEntity entity1 = new MyEntity( 301 );
    final MyEntity entity2 = new MyEntity( 302 );
    final MyEntity entity3 = new MyEntity( 303 );
    final MyEntity entity4 = new MyEntity( 304 );
    final MyEntity entity5 = new MyEntity( 305 );
    final MyContainer repository = MyContainer.create();

    assertFalse( context.safeAction( () -> repository.contains( entity1 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity2 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity3 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity4 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity5 ) ) );

    context.safeAction( () -> repository.attach( entity1 ) );

    assertTrue( context.safeAction( () -> repository.contains( entity1 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity2 ) ) );

    context.safeAction( () -> repository.attach( entity2 ) );
    context.safeAction( () -> repository.attach( entity3 ) );
    context.safeAction( () -> repository.attach( entity4 ) );
    context.safeAction( () -> repository.attach( entity5 ) );

    assertTrue( context.safeAction( () -> repository.contains( entity1 ) ) );
    assertTrue( context.safeAction( () -> repository.contains( entity2 ) ) );
    assertTrue( context.safeAction( () -> repository.contains( entity3 ) ) );
    assertTrue( context.safeAction( () -> repository.contains( entity4 ) ) );
    assertTrue( context.safeAction( () -> repository.contains( entity5 ) ) );

    assertFalse( Disposable.isDisposed( entity1 ) );
    assertFalse( Disposable.isDisposed( entity2 ) );
    assertFalse( Disposable.isDisposed( entity3 ) );
    assertFalse( Disposable.isDisposed( entity4 ) );
    assertFalse( Disposable.isDisposed( entity5 ) );

    context.safeAction( () -> repository.destroy( entity5 ) );

    assertTrue( context.safeAction( () -> repository.contains( entity1 ) ) );
    assertTrue( context.safeAction( () -> repository.contains( entity2 ) ) );
    assertTrue( context.safeAction( () -> repository.contains( entity3 ) ) );
    assertTrue( context.safeAction( () -> repository.contains( entity4 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity5 ) ) );

    assertFalse( Disposable.isDisposed( entity1 ) );
    assertFalse( Disposable.isDisposed( entity2 ) );
    assertFalse( Disposable.isDisposed( entity3 ) );
    assertFalse( Disposable.isDisposed( entity4 ) );
    assertTrue( Disposable.isDisposed( entity5 ) );

    context.safeAction( () -> repository.destroy( entity1 ) );
    context.safeAction( () -> repository.destroy( entity2 ) );
    context.safeAction( () -> repository.destroy( entity3 ) );
    context.safeAction( () -> repository.destroy( entity4 ) );

    assertFalse( context.safeAction( () -> repository.contains( entity1 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity2 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity3 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity4 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity5 ) ) );

    assertTrue( Disposable.isDisposed( entity1 ) );
    assertTrue( Disposable.isDisposed( entity2 ) );
    assertTrue( Disposable.isDisposed( entity3 ) );
    assertTrue( Disposable.isDisposed( entity4 ) );
    assertTrue( Disposable.isDisposed( entity5 ) );
  }

  @Test
  public void disposedEntitiesAreRemovedFromContainer()
  {
    final ArezContext context = Arez.context();

    final MyEntity entity1 = new MyEntity( 301 );
    final MyEntity entity2 = new MyEntity( 302 );
    final MyContainer repository = MyContainer.create();

    assertFalse( context.safeAction( () -> repository.contains( entity1 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity2 ) ) );

    context.safeAction( () -> repository.attach( entity1 ) );

    assertTrue( context.safeAction( () -> repository.contains( entity1 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity2 ) ) );

    context.safeAction( () -> repository.attach( entity2 ) );

    assertTrue( context.safeAction( () -> repository.contains( entity1 ) ) );
    assertTrue( context.safeAction( () -> repository.contains( entity2 ) ) );

    assertFalse( Disposable.isDisposed( entity1 ) );
    assertFalse( Disposable.isDisposed( entity2 ) );

    assertEquals( repository.entityMap().size(), 2 );

    Disposable.dispose( entity1 );

    assertEquals( repository.entityMap().size(), 1 );

    assertFalse( context.safeAction( () -> repository.contains( entity1 ) ) );
    assertTrue( context.safeAction( () -> repository.contains( entity2 ) ) );

    assertTrue( Disposable.isDisposed( entity1 ) );
    assertFalse( Disposable.isDisposed( entity2 ) );
  }

  @Test
  public void detachRemovesEntityFromContainerWithoutDisposing()
  {
    final ArezContext context = Arez.context();

    final MyEntity entity1 = new MyEntity( 301 );
    final MyContainer repository = MyContainer.create();

    final AtomicInteger callCount = new AtomicInteger();
    Arez.context().observer( () -> {
      repository.getEntitiesObservableValue().reportObserved();
      callCount.incrementAndGet();
    } );

    assertEquals( callCount.get(), 1 );

    context.safeAction( () -> repository.attach( entity1 ) );

    assertEquals( callCount.get(), 2 );

    assertTrue( context.safeAction( () -> repository.contains( entity1 ) ) );

    assertFalse( Disposable.isDisposed( entity1 ) );

    assertEquals( callCount.get(), 2 );

    context.safeAction( () -> repository.detach( entity1 ) );

    assertFalse( context.safeAction( () -> repository.contains( entity1 ) ) );

    assertFalse( Disposable.isDisposed( entity1 ) );

    assertEquals( callCount.get(), 3 );
  }

  @Test
  public void preDispose()
  {
    final ArezContext context = Arez.context();

    final MyEntity entity1 = new MyEntity( 301 );
    final MyEntity entity2 = new MyEntity( 302 );
    final MyContainer repository = MyContainer.create();

    context.safeAction( () -> repository.attach( entity1 ) );
    context.safeAction( () -> repository.attach( entity2 ) );

    assertTrue( context.safeAction( () -> repository.contains( entity1 ) ) );
    assertTrue( context.safeAction( () -> repository.contains( entity2 ) ) );

    repository.preDispose();

    assertFalse( context.safeAction( () -> repository.contains( entity1 ) ) );
    assertFalse( context.safeAction( () -> repository.contains( entity2 ) ) );

    assertTrue( Disposable.isDisposed( entity1 ) );
    assertTrue( Disposable.isDisposed( entity2 ) );
  }

  @Test
  public void destroyOnDisposed()
  {
    final MyEntity entity1 = new MyEntity( 301 );
    final MyContainer repository = MyContainer.create();

    assertInvariantFailure( () -> repository.destroy( entity1 ),
                            "Arez-0157: Called detach() passing an entity that was not attached " +
                            "to the container. Entity: " + entity1 );
  }

  @Test
  public void attachWhenDisposed()
  {
    final MyEntity entity1 = new MyEntity( 301 );
    final MyContainer repository = MyContainer.create();

    entity1.dispose();

    assertInvariantFailure( () -> Arez.context().safeAction( () -> repository.attach( entity1 ) ),
                            "Arez-0168: Called attach() passing an entity that is disposed. Entity: " + entity1 );
  }

  @Test
  public void attachWhenPresent()
  {
    final MyEntity entity1 = new MyEntity( 301 );
    final MyContainer repository = MyContainer.create();

    Arez.context().safeAction( () -> repository.attach( entity1 ) );

    assertInvariantFailure( () -> Arez.context().safeAction( () -> repository.attach( entity1 ) ),
                            "Arez-0136: Called attach() passing an entity that is already attached " +
                            "to the container. Entity: " + entity1 );
  }

  @Test
  public void detachWhenNotPresent()
  {
    final MyEntity entity1 = new MyEntity( 301 );
    final MyContainer repository = MyContainer.create();

    assertInvariantFailure( () -> repository.detach( entity1 ),
                            "Arez-0157: Called detach() passing an entity that was not attached " +
                            "to the container. Entity: " + entity1 );
  }

  @Test
  public void findByArezId()
  {
    final ArezContext context = Arez.context();

    final MyContainer repository = MyContainer.create();
    context.safeAction( () -> repository.attach( new MyEntity( 302 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 303 ) ) );

    final MyEntity entity = context.safeAction( () -> repository.findByArezId( 302 ) );
    assertNotNull( entity );
    assertEquals( entity.getArezId(), (Integer) 302 );

    // Dispose entity so that it is no longer found
    entity.dispose();

    final MyEntity entity2 = context.safeAction( () -> repository.findByArezId( 302 ) );
    assertNull( entity2 );
  }

  @Test
  public void getByArezId()
  {
    final ArezContext context = Arez.context();

    final MyContainer repository = MyContainer.create();
    context.safeAction( () -> repository.attach( new MyEntity( 302 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 303 ) ) );

    final MyEntity entity = context.safeAction( () -> repository.getByArezId( 302 ) );
    assertNotNull( entity );
    assertEquals( entity.getArezId(), (Integer) 302 );

    // Dispose entity so that it is no longer found
    entity.dispose();

    // Missing entities generate an exception
    final NoSuchEntityException exception =
      expectThrows( NoSuchEntityException.class, () -> context.safeAction( () -> repository.getByArezId( 302 ) ) );
    assertEquals( exception.getId(), 302 );
  }

  @Test
  public void entities()
  {
    final ArezContext context = Arez.context();

    final MyContainer repository = MyContainer.create();
    context.safeAction( () -> repository.attach( new MyEntity( 302 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 303 ) ) );

    final int[] ids = repository.entities().mapToInt( e -> e.getArezId() ).toArray();
    assertEquals( ids.length, 2 );
    assertEquals( ids[ 0 ], 302 );
    assertEquals( ids[ 1 ], 303 );
  }

  @Test
  public void entities_removesDisposed()
  {
    final ArezContext context = Arez.context();

    final MyContainer repository = MyContainer.create();
    final MyEntity entity = new MyEntity( 302 );
    context.safeAction( () -> repository.attach( entity ) );
    Disposable.dispose( entity );
    context.safeAction( () -> repository.attach( new MyEntity( 303 ) ) );

    final int[] ids = repository.entities().mapToInt( e -> e.getArezId() ).toArray();
    assertEquals( ids.length, 1 );
    assertEquals( ids[ 0 ], 303 );
  }

  static class MyEntity
    implements Identifiable<Integer>, Disposable, ComponentObservable, DisposeNotifier
  {
    private final ObservableValue<Object> _observableValue = Arez.context().observable();
    private final ComponentKernel _kernel;
    private int _arezId;

    MyEntity( final int arezId )
    {
      _arezId = arezId;
      final ArezContext context = Arez.context();
      _kernel = new ComponentKernel( context,
                                     "MyType",
                                     arezId,
                                     context.component( "MyType",
                                                        arezId,
                                                        "MyType@" + arezId,
                                                        this::notifyOnDisposeListeners ),
                                     null,
                                     null,
                                     null,
                                     true,
                                     false,
                                     false );
    }

    private void notifyOnDisposeListeners()
    {
      _kernel.notifyOnDisposeListeners();
    }

    @Override
    public void addOnDisposeListener( @Nonnull final Object key, @Nonnull final SafeProcedure action )
    {
      _kernel.addOnDisposeListener( key, action );
    }

    @Override
    public void removeOnDisposeListener( @Nonnull final Object key )
    {
      _kernel.removeOnDisposeListener( key );
    }

    @Override
    public boolean observe()
    {
      final boolean isDisposed = isDisposed();
      if ( !isDisposed )
      {
        _observableValue.reportObserved();
      }
      return !isDisposed;
    }

    @Override
    public void dispose()
    {
      _kernel.dispose();
    }

    @Override
    public boolean isDisposed()
    {
      return _kernel.isDisposed();
    }

    @Nonnull
    @Override
    public Integer getArezId()
    {
      return _arezId;
    }
  }

  static class MyContainer
    extends AbstractContainer<Integer, MyEntity>
  {
    private final ObservableValue<Stream<MyEntity>> _observableValue = Arez.context().observable();

    static MyContainer create()
    {
      return new MyContainer();
    }

    @Override
    protected final boolean shouldDisposeEntryOnDispose()
    {
      return true;
    }

    @Nonnull
    @Override
    protected ObservableValue<Stream<MyEntity>> getEntitiesObservableValue()
    {
      return _observableValue;
    }
  }
}
