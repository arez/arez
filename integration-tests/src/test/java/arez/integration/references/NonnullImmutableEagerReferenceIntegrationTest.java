package arez.integration.references;

import arez.Arez;
import arez.Disposable;
import arez.annotations.Action;
import arez.annotations.ArezComponent;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.annotations.Repository;
import arez.component.Identifiable;
import arez.component.TypeBasedLocator;
import arez.integration.AbstractArezIntegrationTest;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NonnullImmutableEagerReferenceIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final NonnullImmutableEagerReferenceIntegrationTest_Model2Repository repository =
      NonnullImmutableEagerReferenceIntegrationTest_Model2Repository.newRepository();

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
    assertEquals( findCallCount.get(), 1 );

    assertEquals( model1.getModel2(), model2a );

    assertEquals( findCallCount.get(), 1 );

    Disposable.dispose( model1 );

    assertInvariant( model1::getModel2, "Method named 'getModel2' invoked on disposed component named 'Model1.0'" );
  }

  @ArezComponent
  static abstract class Model1
  {
    @Nonnull
    private final Object _model2Id;

    static Model1 create( @Nonnull final Object model2Id )
    {
      return new NonnullImmutableEagerReferenceIntegrationTest_Arez_Model1( model2Id );
    }

    Model1( @Nonnull final Object model2Id )
    {
      _model2Id = model2Id;
    }

    @Reference
    abstract Model2 getModel2();

    @ReferenceId
    @Nonnull
    final Object getModel2Id()
    {
      return _model2Id;
    }
  }

  @Repository
  @ArezComponent
  static abstract class Model2
  {
    @Action
    void doStuff()
    {
    }
  }
}
