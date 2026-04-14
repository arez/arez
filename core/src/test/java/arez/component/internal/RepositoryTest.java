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
import arez.component.NoResultException;
import arez.component.NoSuchEntityException;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class RepositoryTest
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
    final MyRepository repository = MyRepository.create();

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
  public void preDispose()
  {
    final ArezContext context = Arez.context();

    final MyEntity entity1 = new MyEntity( 301 );
    final MyEntity entity2 = new MyEntity( 302 );
    final MyRepository repository = MyRepository.create();

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
  public void findAll()
  {
    final ArezContext context = Arez.context();

    final MyRepository repository = MyRepository.create();
    context.safeAction( () -> repository.attach( new MyEntity( 302 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 303 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 304 ) ) );
    final MyEntity entity = new MyEntity( 305 );
    context.safeAction( () -> repository.attach( entity ) );
    entity.dispose();

    final Set<Integer> ids =
      context.safeAction( () -> repository.findAll()
        .stream()
        .map( MyEntity::getArezId )
        .collect( Collectors.toSet() ) );
    assertEquals( ids.size(), 3 );
    assertTrue( ids.contains( 302 ) );
    assertTrue( ids.contains( 303 ) );
    assertTrue( ids.contains( 304 ) );
  }

  @Test
  public void findAllSorted()
  {
    final ArezContext context = Arez.context();

    final MyRepository repository = MyRepository.create();
    context.safeAction( () -> repository.attach( new MyEntity( 302 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 303 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 304 ) ) );
    final MyEntity entity = new MyEntity( 305 );
    context.safeAction( () -> repository.attach( entity ) );
    entity.dispose();

    final int[] ids =
      context.safeAction( () -> repository.findAll( Comparator.comparing( e -> -e.getArezId() ) )
        .stream().mapToInt( MyEntity::getArezId ).toArray() );
    assertEquals( ids.length, 3 );
    assertEquals( ids[ 0 ], 304 );
    assertEquals( ids[ 1 ], 303 );
    assertEquals( ids[ 2 ], 302 );
  }

  @Test
  public void findAllByQuery()
  {
    final ArezContext context = Arez.context();

    final MyRepository repository = MyRepository.create();
    context.safeAction( () -> repository.attach( new MyEntity( 302 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 303 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 304 ) ) );
    final MyEntity entity = new MyEntity( 305 );
    context.safeAction( () -> repository.attach( entity ) );
    entity.dispose();

    final Set<Integer> ids =
      context.safeAction( () -> repository.findAllByQuery( e -> e.getArezId() <= 303 )
        .stream().map( MyEntity::getArezId ).collect( Collectors.toSet() ) );
    assertEquals( ids.size(), 2 );
    assertTrue( ids.contains( 302 ) );
    assertTrue( ids.contains( 303 ) );
  }

  @Test
  public void findAllByQuerySorted()
  {
    final ArezContext context = Arez.context();

    final MyRepository repository = MyRepository.create();
    context.safeAction( () -> repository.attach( new MyEntity( 300 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 301 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 302 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 303 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 304 ) ) );
    final MyEntity entity = new MyEntity( 305 );
    context.safeAction( () -> repository.attach( entity ) );
    entity.dispose();

    final int[] ids =
      context.safeAction( () -> repository.findAllByQuery( e -> e.getArezId() <= 303,
                                                           Comparator.comparing( e -> -e.getArezId() ) )
        .stream().mapToInt( MyEntity::getArezId ).toArray() );
    assertEquals( ids.length, 4 );
    assertEquals( ids[ 0 ], 303 );
    assertEquals( ids[ 1 ], 302 );
    assertEquals( ids[ 2 ], 301 );
    assertEquals( ids[ 3 ], 300 );
  }

  @Test
  public void findByQuery()
  {
    final ArezContext context = Arez.context();

    final MyRepository repository = MyRepository.create();
    context.safeAction( () -> repository.attach( new MyEntity( 302 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 303 ) ) );

    final MyEntity entity = context.safeAction( () -> repository.findByQuery( e -> e.getArezId() == 302 ) );
    assertNotNull( entity );
    assertEquals( entity.getArezId(), (Integer) 302 );

    // Dispose entity so that it is no longer found
    entity.dispose();

    final MyEntity entity2 = context.safeAction( () -> repository.findByQuery( e -> e.getArezId() == 302 ) );
    assertNull( entity2 );
  }

  @Test
  public void getByQuery()
  {
    final ArezContext context = Arez.context();

    final MyRepository repository = MyRepository.create();
    context.safeAction( () -> repository.attach( new MyEntity( 302 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 303 ) ) );

    final MyEntity entity = context.safeAction( () -> repository.getByQuery( e -> e.getArezId() == 302 ) );
    assertNotNull( entity );
    assertEquals( entity.getArezId(), (Integer) 302 );

    // Dispose entity so that it is no longer found
    entity.dispose();

    assertThrows( NoResultException.class,
                  () -> context.safeAction( () -> repository.getByQuery( e -> e.getArezId() == 302 ) ) );
  }

  @Test
  public void findByArezId()
  {
    final ArezContext context = Arez.context();

    final MyRepository repository = MyRepository.create();
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

    final MyRepository repository = MyRepository.create();
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
  public void self()
  {
    final MyRepository repository = MyRepository.create();
    assertEquals( repository.self(), repository );
  }

  @Test
  public void entities()
  {
    final ArezContext context = Arez.context();

    final MyRepository repository = MyRepository.create();
    context.safeAction( () -> repository.attach( new MyEntity( 302 ) ) );
    context.safeAction( () -> repository.attach( new MyEntity( 303 ) ) );

    final int[] ids = context.safeAction( () -> repository.entities().mapToInt( MyEntity::getArezId ).toArray() );
    assertEquals( ids.length, 2 );
    assertEquals( ids[ 0 ], 302 );
    assertEquals( ids[ 1 ], 303 );
  }

  @Test
  public void entities_removesDisposed()
  {
    final ArezContext context = Arez.context();

    final MyRepository repository = MyRepository.create();
    final MyEntity entity = new MyEntity( 302 );

    context.safeAction( () -> repository.attach( entity ) );
    context.safeAction( () -> repository.attach( new MyEntity( 303 ) ) );

    Disposable.dispose( entity );

    final int[] ids = context.safeAction( () -> repository.entities().mapToInt( MyEntity::getArezId ).toArray() );
    assertEquals( ids.length, 1 );
    assertEquals( ids[ 0 ], 303 );
  }

  static class MyEntity
    implements Identifiable<Integer>, Disposable, ComponentObservable, DisposeNotifier
  {
    private final int _arezId;
    private final ObservableValue<Object> _observableValue = Arez.context().observable();
    private final ComponentKernel _kernel;

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
    public void addOnDisposeListener( @Nonnull final Object key,
                                      @Nonnull final SafeProcedure action,
                                      final boolean errorIfDuplicate )
    {
      _kernel.addOnDisposeListener( key, action, errorIfDuplicate );
    }

    @Override
    public void removeOnDisposeListener( @Nonnull final Object key, final boolean errorIfMissing )
    {
      _kernel.removeOnDisposeListener( key, errorIfMissing );
    }

    @Override
    public boolean observe()
    {
      _observableValue.reportObserved();
      return isNotDisposed();
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

  static class MyRepository
    extends AbstractRepository<Integer, MyEntity, MyRepository>
  {
    private final ObservableValue<Stream<MyEntity>> _observableValue = Arez.context().observable();

    static MyRepository create()
    {
      return new MyRepository();
    }

    @Nonnull
    @Override
    protected ObservableValue<?> getEntitiesObservableValue()
    {
      return _observableValue;
    }

    @Nonnull
    @Override
    public Stream<MyEntity> entities()
    {
      _observableValue.reportObserved();
      return super.entities();
    }
  }
}
