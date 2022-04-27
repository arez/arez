package arez.integration.references;

import arez.Arez;
import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Feature;
import arez.annotations.LinkType;
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

public final class NonnullImmutableLazyReferenceIntegrationTest
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
    final Object model2aId = Objects.requireNonNull( Identifiable.getArezId( model2a ) );
    entities.put( model2aId, model2a );

    assertEquals( findCallCount.get(), 0 );
    final Model1 model1 = Model1.create( model2aId );
    assertEquals( findCallCount.get(), 0 );

    assertEquals( model1.getModel2(), model2a );

    assertEquals( findCallCount.get(), 1 );

    Disposable.dispose( model1 );

    assertInvariant( model1::getModel2,
                     "Method named 'getModel2' invoked on disposed component named 'arez_integration_references_NonnullImmutableLazyReferenceIntegrationTest_Model1.1'" );
  }

  @ArezComponent
  static abstract class Model1
  {
    @Nonnull
    private final Object _model2Id;

    static Model1 create( @Nonnull final Object model2Id )
    {
      return new NonnullImmutableLazyReferenceIntegrationTest_Arez_Model1( model2Id );
    }

    Model1( @Nonnull final Object model2Id )
    {
      _model2Id = model2Id;
    }

    @Reference( load = LinkType.LAZY )
    abstract Model2 getModel2();

    @ReferenceId
    @Nonnull
    Object getModel2Id()
    {
      return _model2Id;
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

  @ArezComponent( service = Feature.ENABLE, sting = Feature.DISABLE )
  static abstract class Model2Repository
    extends AbstractRepository<Integer, Model2, Model2Repository>
  {
    static Model2Repository newRepository()
    {
      return new NonnullImmutableLazyReferenceIntegrationTest_Arez_Model2Repository();
    }

    @Action
    Model2 create()
    {
      final NonnullImmutableLazyReferenceIntegrationTest_Arez_Model2 entity =
        new NonnullImmutableLazyReferenceIntegrationTest_Arez_Model2();
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
