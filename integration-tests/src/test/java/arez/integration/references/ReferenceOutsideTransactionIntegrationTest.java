package arez.integration.references;

import arez.Arez;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.component.internal.AbstractRepository;
import arez.integration.AbstractArezIntegrationTest;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class ReferenceOutsideTransactionIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
  {
    final Model2Repository repository = Model2Repository.newRepository();

    final TypeBasedLocator locator = new TypeBasedLocator();
    Arez.context().registerLocator( locator );

    final AtomicInteger findCallCount = new AtomicInteger();
    final HashMap<Object, Model2> entities = new HashMap<>();
    locator.registerLookup( Model2.class, id -> {
      findCallCount.incrementAndGet();
      return entities.get( id );
    } );

    final Model2 model2a = repository.create();
    final Model2 model2b = repository.create();
    final Object model2aId = Objects.requireNonNull( Identifiable.getArezId( model2a ) );
    final Object model2bId = Objects.requireNonNull( Identifiable.getArezId( model2b ) );
    entities.put( model2aId, model2a );
    entities.put( model2bId, model2b );

    assertEquals( findCallCount.get(), 0 );
    final Model1 model1 = Model1.create( model2aId );
    assertEquals( findCallCount.get(), 1 );

    assertEquals( model1.getModel2(), model2a );

    assertEquals( findCallCount.get(), 1 );

    final AtomicInteger observerCallCount = new AtomicInteger();
    observer( () -> {
      model1.getModel2();
      observerCallCount.incrementAndGet();
    } );

    assertEquals( observerCallCount.get(), 1 );
    assertEquals( findCallCount.get(), 1 );

    safeAction( () -> model1.setModel2Id( model2bId ) );

    assertEquals( observerCallCount.get(), 2 );
    assertEquals( findCallCount.get(), 2 );

    assertEquals( model1.getModel2(), model2b );

    assertEquals( observerCallCount.get(), 2 );
    assertEquals( findCallCount.get(), 2 );
  }

  @ArezComponent
  static abstract class Model1
  {
    @Nonnull
    private Object _model2Id;

    static Model1 create( @Nonnull final Object model2Id )
    {
      return new ReferenceOutsideTransactionIntegrationTest_Arez_Model1( model2Id );
    }

    Model1( @Nonnull final Object model2Id )
    {
      _model2Id = model2Id;
    }

    @Reference
    abstract Model2 getModel2();

    @ReferenceId
    @Observable( readOutsideTransaction = Feature.ENABLE )
    @Nonnull
    Object getModel2Id()
    {
      return _model2Id;
    }

    void setModel2Id( @Nonnull Object model2Id )
    {
      _model2Id = model2Id;
    }
  }

  @ArezComponent( requireId = Feature.ENABLE )
  static abstract class Model2
  {
    @Action
    void doStuff()
    {
    }
  }

  @ArezComponent( service = Feature.ENABLE, dagger = Feature.DISABLE, sting = Feature.DISABLE )
  static abstract class Model2Repository
    extends AbstractRepository<Integer, Model2, Model2Repository>
  {
    @Nonnull
    static Model2Repository newRepository()
    {
      return new ReferenceOutsideTransactionIntegrationTest_Arez_Model2Repository();
    }

    @Action
    Model2 create()
    {
      final ReferenceOutsideTransactionIntegrationTest_Arez_Model2 entity =
        new ReferenceOutsideTransactionIntegrationTest_Arez_Model2();
      attach( entity );
      return entity;
    }

    @Action
    protected void destroy( @Nonnull final Model2 entity )
    {
      super.destroy( entity );
    }
  }
}
