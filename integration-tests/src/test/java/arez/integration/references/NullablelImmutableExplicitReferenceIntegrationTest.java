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
import arez.component.Linkable;
import arez.component.TypeBasedLocator;
import arez.component.internal.AbstractRepository;
import arez.integration.AbstractArezIntegrationTest;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public final class NullablelImmutableExplicitReferenceIntegrationTest
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

    assertInvariant( model1::getModel2,
                     "Nullable reference method named 'getModel2' invoked on component named 'arez_integration_references_NullablelImmutableExplicitReferenceIntegrationTest_Model1.1' and reference has not been resolved yet is not lazy. Id = 1" );

    Linkable.link( model1 );

    assertEquals( findCallCount.get(), 1 );

    assertEquals( model1.getModel2(), model2a );

    assertEquals( findCallCount.get(), 1 );

    Disposable.dispose( model1 );

    assertInvariant( model1::getModel2,
                     "Method named 'getModel2' invoked on disposed component named 'arez_integration_references_NullablelImmutableExplicitReferenceIntegrationTest_Model1.1'" );

    findCallCount.set( 0 );

    final Model1 model1b = Model1.create( null );
    assertEquals( findCallCount.get(), 0 );

    assertNull( model1b.getModel2() );

    assertEquals( findCallCount.get(), 0 );
  }

  @ArezComponent
  static abstract class Model1
  {
    @Nullable
    private final Object _model2Id;

    static Model1 create( @Nullable final Object model2Id )
    {
      return new NullablelImmutableExplicitReferenceIntegrationTest_Arez_Model1( model2Id );
    }

    Model1( @Nullable final Object model2Id )
    {
      _model2Id = model2Id;
    }

    @Reference( load = LinkType.EXPLICIT )
    abstract Model2 getModel2();

    @ReferenceId
    @Nullable
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

  @ArezComponent( service = Feature.ENABLE, dagger = Feature.DISABLE, sting = Feature.DISABLE )
  static abstract class Model2Repository
    extends AbstractRepository<Integer, Model2, Model2Repository>
  {
    static Model2Repository newRepository()
    {
      return new NullablelImmutableExplicitReferenceIntegrationTest_Arez_Model2Repository();
    }

    @Action
    Model2 create()
    {
      final NullablelImmutableExplicitReferenceIntegrationTest_Arez_Model2 entity =
        new NullablelImmutableExplicitReferenceIntegrationTest_Arez_Model2();
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
