package arez.integration.references;

import arez.Arez;
import arez.Disposable;
import arez.Observer;
import arez.annotations.ArezComponent;
import arez.annotations.LinkType;
import arez.annotations.Observable;
import arez.annotations.Reference;
import arez.annotations.ReferenceId;
import arez.annotations.Repository;
import arez.component.Identifiable;
import arez.component.Linkable;
import arez.component.TypeBasedLocator;
import arez.integration.AbstractArezIntegrationTest;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class NullableObservableExplicitReferenceIntegrationTest
  extends AbstractArezIntegrationTest
{
  @Test
  public void scenario()
    throws Throwable
  {
    final NullableObservableExplicitReferenceIntegrationTest_Model2Repository repository =
      NullableObservableExplicitReferenceIntegrationTest_Model2Repository.newRepository();

    final TypeBasedLocator locator = new TypeBasedLocator();
    Arez.context().registerLocator( locator );

    final AtomicInteger findCallCount = new AtomicInteger();
    final HashMap<Object, Model2> entities = new HashMap<>();
    locator.registerLookup( Model2.class, id -> {
      findCallCount.incrementAndGet();
      return entities.get( id );
    } );

    final Model2 model2b = repository.create();
    final Object model2bId = Objects.requireNonNull( Identifiable.getArezId( model2b ) );
    entities.put( model2bId, model2b );

    assertEquals( findCallCount.get(), 0 );
    final Model1 model1 = Model1.create( null );
    assertEquals( findCallCount.get(), 0 );

    safeAction( () -> assertEquals( model1.getModel2(), null ) );

    assertEquals( findCallCount.get(), 0 );

    final AtomicInteger observerCallCount = new AtomicInteger();
    final Observer observer = autorun( () -> {
      model1.getModel2();
      observerCallCount.incrementAndGet();
    } );

    assertEquals( observerCallCount.get(), 1 );

    safeAction( () -> {
      model1.setModel2Id( model2bId );
      assertEquals( findCallCount.get(), 0 );
      Linkable.link( model1 );
      assertEquals( findCallCount.get(), 1 );
    } );

    assertEquals( observerCallCount.get(), 2 );
    assertEquals( findCallCount.get(), 1 );

    safeAction( () -> assertEquals( model1.getModel2(), model2b ) );

    safeAction( () -> model1.setModel2Id( null ) );

    assertEquals( observerCallCount.get(), 3 );
    assertEquals( findCallCount.get(), 1 );

    safeAction( () -> assertEquals( model1.getModel2(), null ) );

    // Dispose to avoid error
    observer.dispose();

    Disposable.dispose( model1 );

    assertInvariant( model1::getModel2, "Method named 'getModel2' invoked on disposed component named 'Model1.0'" );
  }

  @ArezComponent
  static abstract class Model1
  {
    @Nullable
    private Object _model2Id;

    static Model1 create( @Nullable final Object model2Id )
    {
      return new NullableObservableExplicitReferenceIntegrationTest_Arez_Model1( model2Id );
    }

    Model1( @Nullable final Object model2Id )
    {
      _model2Id = model2Id;
    }

    @Reference( load = LinkType.EXPLICIT )
    abstract Model2 getModel2();

    @ReferenceId
    @Observable
    @Nullable
    Object getModel2Id()
    {
      return _model2Id;
    }

    void setModel2Id( @Nullable Object model2Id )
    {
      _model2Id = model2Id;
    }
  }

  @Repository
  @ArezComponent( allowEmpty = true )
  static abstract class Model2
  {
  }
}
